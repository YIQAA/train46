package com.qdu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 智能客服对话记录
 * </p>
 *
 * @author 徐增润
 * @since 2025-03-23
 */
@Getter
@Setter
@TableName("customer_service_sessions")
public class CustomerServiceSessions implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "session_id", type = IdType.AUTO)
    private Integer sessionId;

    private Integer userId;

    /**
     * 用户意图分类
     */
    private String intentCategory;

    /**
     * 对话内容
     */
    private String dialogContent;

    /**
     * 提取的参数
     */
    private String extractedParams;

    private LocalDateTime createdAt;
}
