package com.qdu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 * 订单主表
 * </p>
 *
 * @author 徐增润
 * @since 2025-03-28
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    @TableId(value = "order_id", type = IdType.AUTO)
    private Integer orderId;

    /**
     * 16位订单号
     */
    private String orderNumber;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 关联车次ID
     */
    private Integer trainId;

    /**
     * 车次编号
     */
    private String trainNumber;

    /**
     * 发车日期
     */
    private LocalDate trainDate;

    /**
     * 出发站ID
     */
    private Integer startStationId;

    /**
     * 到达站ID
     */
    private Integer endStationId;

    /**
     * 出发时间
     */
    private LocalTime departureTime;

    /**
     * 到达时间
     */
    private LocalTime arrivalTime;

    /**
     * 支付金额
     */
    private Integer paymentAmount;

    /**
     * 订单状态 0待支付 1已支付 2已取消 3已完成
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
