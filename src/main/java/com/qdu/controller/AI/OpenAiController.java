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
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import java.time.LocalDate;

/**
 * 控制器类，处理与AI聊天相关的请求
 */
@RestController
@CrossOrigin
@RequestMapping("/api/AIchat-service")
public class OpenAiController  {

    // 聊天客户端对象，用于与AI模型进行交互
    private final ChatClient chatClient;

    /**
     * 构造函数，初始化聊天客户端
     * @param chatClientBuilder 聊天客户端构建器
     * @param chatMemory 聊天记忆对象，用于存储聊天上下文
     * @param vectorStore 向量存储对象，用于存储文本的向量表示
     */
    public OpenAiController(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory, VectorStore vectorStore) {
        // 设置系统提示信息，告知AI模型的角色和任务
        this.chatClient = chatClientBuilder.defaultSystem(
                        """
                                您是火车订票系统的客户聊天支持代理。请以友好、乐于助人且愉快的方式来回复。
                                您正在通过在线聊天系统与客户互动。
                                在提供查询车票列表之前，您必须始终
                                从用户处获取以下信息：出发城市、目的城市、出发日期。
                                在询问用户之前，请检查消息历史记录以获取此信息。
                                在订票之前，请先获取车次信息及乘客姓名、证件号等并且用户确定之后才进行订票。
                                请讲中文。
                                今天的日期是 {current_date}.
                                """
                )

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
     * 处理用户的聊天请求，返回AI生成的响应流
     * @param message 用户输入的消息，默认为“讲个笑话”
     * @return AI生成的响应流
     */
    @CrossOrigin
    @GetMapping(value = "/generateStreamAsString")
    public String generateStreamAsString(@RequestParam(value = "message", defaultValue = "讲个笑话") String message) {
        System.out.println("啦啦啦啦啦啦啦啦啦AI   message:"+message);

        // 构建聊天请求，包括用户消息、系统提示信息和顾问参数
        return this.chatClient.prompt()
                .user(message)
                .system(spec -> spec.param("current_date", LocalDate.now().toString()))
                .advisors(advisorSpec -> advisorSpec.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100))
                .call()
                .content();
    }
}