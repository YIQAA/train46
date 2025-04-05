
package com.qdu.dto.resp.order;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 车票订单详情返回参数
 *
 */
@Data
public class TicketOrderDetailRespDTO {

    /**
     * 订单号
     */
    private String orderSn;

    /**
     * 列车 ID
     */
    private Integer trainId;

    /**
     * 出发站点
     */
    private String departureStation;

    /**
     * 到达站点
     */
    private String arrivalStation;

    /**
     * 乘车日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate ridingDate;

    /**
     * 订票时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime orderTime;

    /**
     * 列车车次
     */
    private String trainNumber;

    /**
     * 出发时间
     */
    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
    private LocalTime departureTime;

    /**
     * 到达时间
     */
    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
    private LocalTime arrivalTime;

    /**
     * 乘车人订单详情
     */
    private List<TicketOrderPassengerDetailRespDTO> passengerDetails;
}
