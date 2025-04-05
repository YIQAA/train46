package com.qdu.utils;

import org.springframework.ai.chat.client.AdvisedRequest;
import org.springframework.ai.chat.client.RequestResponseAdvisor;

import java.util.Map;
/**
 * 日志顾问类，用于记录聊天请求信息
 */
public class LoggingAdvisor implements RequestResponseAdvisor {

    /**
     * 处理聊天请求，记录请求信息并返回原请求
     * @param request 聊天请求对象
     * @param context 上下文信息
     * @return 原聊天请求对象
     */
    @Override
    public AdvisedRequest adviseRequest(AdvisedRequest request, Map<String, Object> context) {
        // 打印聊天请求信息
        System.out.println("Request::" + request);
        return request;
    }
}