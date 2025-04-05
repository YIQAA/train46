package com.qdu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 座位类型配置表
 * </p>
 *
 * @author 徐增润
 * @since 2025-03-23
 */
@Getter
@Setter
@TableName("seat_config")
public class SeatConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 座位类型ID
     */
    @TableId(value = "type_id", type = IdType.AUTO)
    private Integer typeId;

    /**
     * 类型名称（如：一等座、硬卧）
     */
    private String typeName;

    /**
     * 每车厢座位数
     */
    private Integer seatsPerCarriage;

    /**
     * 基础价格（元/公里）
     */
    private BigDecimal basePrice;

    /**
     * 类型描述
     */
    private String description;
}
