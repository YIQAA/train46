
package com.qdu.dto.req.order;

import lombok.Data;

/**
 * 车票订单详情创建请求参数
 *
 */
@Data
public class TicketOrderItemCreateReqDTO {

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 证件类型
     */
    private Integer idType;

    /**
     * 证件号
     */
    private String idCard;

    /**
     * 车票类型 0成人票 1学生票
     */
    private Integer ticketType;

    /**
     * 选择的座位类型
     */
    private Integer seatType;
}
