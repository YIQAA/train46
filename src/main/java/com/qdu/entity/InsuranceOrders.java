package com.qdu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 保险订单表
 * </p>
 *
 * @author 徐增润
 * @since 2025-04-18
 */
@Getter
@Setter
@TableName("insurance_orders")
public class InsuranceOrders implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 保险订单ID
     */
    @TableId(value = "insurance_order_id", type = IdType.AUTO)
    private Integer insuranceOrderId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 保险类别
     */
    private Integer insuranceType;

    /**
     * 保险天数
     */
    private Integer days;

    /**
     * 保险金额
     */
    private Integer amount;

    /**
     * 被保险人姓名
     */
    private String insuredName;

    /**
     * 被保险人证件号
     */
    private String insuredIdCard;

    /**
     * 被保险人手机号
     */
    private String insuredPhone;

    /**
     * 投保人姓名
     */
    private String policyHolderName;

    /**
     * 投保人证件号
     */
    private String policyHolderIdCard;

    /**
     * 投保人手机号
     */
    private String policyHolderPhone;

    /**
     * 起保时间
     */
    private LocalDate startTime;

    /**
     * 终保时间
     */
    private LocalDate endTime;

    /**
     * 状态
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
