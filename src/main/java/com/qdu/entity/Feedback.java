package com.qdu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户意见反馈表
 * </p>
 *
 * @author 徐增润
 * @since 2025-05-04
 */
@Getter
@Setter
public class Feedback implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 反馈ID
     */
    @TableId(value = "feedback_id", type = IdType.AUTO)
    private Integer feedbackId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 反馈类型
     */
    private String feedbackType;

    /**
     * 反馈标题
     */
    private String title;

    /**
     * 反馈内容
     */
    private String content;

    /**
     * 联系方式（非必填）
     */
    private String contactInfo;

    /**
     * 处理状态
     */
    private String status;

    /**
     * 提交时间
     */
    private LocalDateTime createdAt;

    /**
     * 处理时间
     */
    private LocalDateTime processedAt;

    /**
     * 处理回复内容
     */
    private String replyContent;
}
