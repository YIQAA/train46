package com.qdu;



import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

@SpringBootApplication
public class TrainApplication {

    /**
     * 应用程序入口，启动Spring Boot应用
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(TrainApplication.class, args);
    }

    /**
     * 创建一个基于内存的聊天记忆对象，用于存储聊天上下文
     * @return 聊天记忆对象
     */
    @Bean
    public ChatMemory chatMemory(){
        return new InMemoryChatMemory();
    }

    /**
     * 创建一个简单的向量存储对象，用于存储文本的向量表示
     * @param embeddingModel 嵌入模型，用于将文本转换为向量
     * @return 向量存储对象
     */
    @Bean
    public VectorStore vectorStore(EmbeddingModel embeddingModel) {
        return new SimpleVectorStore(embeddingModel);
    }

    /**
     * 定义一个命令行运行器，在Spring Boot应用启动时执行
     * 该方法将terms-of-service.txt文件中的文本读取、转换为向量并写入向量存储中
     * @param embeddingModel 嵌入模型，用于将文本转换为向量
     * @param vectorStore 向量存储对象，用于存储文本的向量表示
     * @param termsOfServiceDocs terms-of-service.txt文件资源
     * @return 命令行运行器对象
     */
    @Bean
    CommandLineRunner ingestTermOfServiceToVectorStore(EmbeddingModel embeddingModel, VectorStore vectorStore,
                                                       @Value("classpath:rag/terms-of-service.txt") Resource termsOfServiceDocs) {

        return args -> {
            // 1. 读取terms-of-service.txt文件中的文本
            // 2. 使用TokenTextSplitter将文本分割成合适的块
            // 3. 将分割后的文本块转换为向量并写入向量存储中
            vectorStore.write(                                  // 3.写入
                    new TokenTextSplitter().transform(          // 2.转换
                            new TextReader(termsOfServiceDocs).read())  // 1.读取
            );
        };
    }
}
