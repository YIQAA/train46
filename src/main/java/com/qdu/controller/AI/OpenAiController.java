package com.qdu.controller.AI;

import com.qdu.utils.LoggingAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Files;
import java.time.Instant;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
/**
 * 控制器类，处理与AI聊天相关的请求
 */
@RestController
@CrossOrigin
@RequestMapping("/api/AIchat-service")
public class OpenAiController {

    // 聊天客户端对象，用于与AI模型进行交互
    private final ChatClient chatClient;
    // JSON序列化/反序列化工具
    private final ObjectMapper objectMapper = new ObjectMapper();
    // 系统提示模板，用于描述AI模型的角色和任务
    private String systemPromptTemplate ;

    /**
     * 构造函数，初始化聊天客户端
     * @param chatClientBuilder 聊天客户端构建器
     * @param chatMemory 聊天记忆对象，用于存储聊天上下文
     * @param vectorStore 向量存储对象，用于存储文本的向量表示
     */
    public OpenAiController(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory, VectorStore vectorStore) {
        try {
            // 读取提示词文件
            File file = ResourceUtils.getFile("classpath:prompt-templates/system-prompt.txt");
            this.systemPromptTemplate = Files.readString(file.toPath());
        } catch (Exception e) {
            throw new RuntimeException("Failed to load system prompt template", e);
        }

        // 设置系统提示信息，告知AI模型的角色和任务
        this.chatClient = chatClientBuilder.defaultSystem(systemPromptTemplate)

                // 添加默认的顾问，用于处理聊天请求和响应
                .defaultAdvisors(
                        // 聊天记忆顾问，用于从聊天记忆中获取上下文信息
                        new PromptChatMemoryAdvisor(chatMemory),
                        // 日志顾问，用于记录聊天请求信息
                        new LoggingAdvisor(),
                        // 问答顾问，用于从向量存储中检索相关信息
                        new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults()) // RAG
                )
                // 设置可用的函数，AI模型可以调用这些函数来执行特定任务
                .defaultFunctions("listTicketPageQueryForAI")
                .build();
    }


    /**
     * 处理用户的聊天请求，返回AI生成的响应
     * @param message 用户输入的消息，默认为“讲个笑话”
     * @return AI生成的响应
     */
    @CrossOrigin
    @GetMapping(value = "/generateAnswer", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> generateAnswer(
            @RequestParam(value = "message") String message) {

        Map<String, Object> finalResponse = new LinkedHashMap<>();
        finalResponse.put("timestamp", Instant.now().toString());

        try {
            // 调用AI并获取结构化响应
            String aiRawResponse = this.chatClient.prompt()
                    .user(message)
                    .system(spec -> spec.param("current_date", LocalDate.now().toString()))
                    .call()
                    .content();
            System.out.println(aiRawResponse);

            // 解析AI返回的原始JSON
            Map<String, Object> aiResponse = objectMapper.readValue(aiRawResponse,
                    new TypeReference<Map<String, Object>>() {});

            // 统一封装为前端需要的结构
            finalResponse.put("status", "success");
            finalResponse.put("type", aiResponse.get("type"));
            finalResponse.put("content", aiResponse.get("content"));

            return ResponseEntity.ok(finalResponse);

        } catch (JsonProcessingException e) {
            // AI返回格式错误时的处理
            finalResponse.put("status", "error");
            finalResponse.put("type", "error");
            finalResponse.put("content", Map.of(
                    "text", "AI响应格式异常，请稍后重试",
                    "code", "AI_FORMAT_ERROR"
            ));
            return ResponseEntity.status(500).body(finalResponse);
        } catch (Exception e) {
            // 其他异常处理
            finalResponse.put("status", "error");
            finalResponse.put("type", "error");
            finalResponse.put("content", Map.of(
                    "text", "系统繁忙，请稍后重试",
                    "code", "SERVER_ERROR"
            ));
            return ResponseEntity.status(500).body(finalResponse);
        }
    }
}