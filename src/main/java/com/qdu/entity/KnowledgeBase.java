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
 * 业务规则知识库
 * </p>
 *
 * @author 徐增润
 * @since 2025-03-23
 */
@Getter
@Setter
@TableName("knowledge_base")
public class KnowledgeBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String ruleType;

    /**
     * 逗号分隔的关键词
     */
    private String keywordsTags;

    private String ruleDetails;

    private String status;

    private LocalDateTime updatedAt;
}
