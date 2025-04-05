package com.qdu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 订单乘客信息表
 * </p>
 *
 * @author 徐增润
 * @since 2025-03-28
 */
@Getter
@Setter
@TableName("order_passengers")
public class OrderPassengers implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 乘客记录ID
     */
    @TableId(value = "passenger_id", type = IdType.AUTO)
    private Integer passengerId;

    /**
     * 所属订单ID
     */
    private Integer orderId;

    /**
     * 物理座位ID
     */
    private Integer seatId;

    /**
     * 乘车人姓名
     */
    private String passengerName;

    /**
     * 证件类型
     */
    private Integer passengerIdType;

    /**
     * 证件号
     */
    private String passengerIdCard;

    /**
     * 票种类型，0成人票，1学生票
     */
    private Integer ticketType;

    /**
     * 车票金额
     */
    private Integer amount;
}
