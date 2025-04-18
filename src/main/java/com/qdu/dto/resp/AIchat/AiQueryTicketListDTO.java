package com.qdu.dto.resp.AIchat;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AiQueryTicketListDTO {

    /**
     * 列车 ID
     */
    private String trainId;

    /**
     * 车次
     */
    private String trainNumber;

    /**
     * 出发站 / 到达站
     */
    private String departureArrivalStation;

    /**
     * 出发 - 到达时间
     */
    private String departureArrivalTime;

    /**
     * 时长
     */
    private String duration;

    /**
     * 余票状态及票价：充足、紧张、无票，xx元起
     */
    private String ticketAvailPrice;

    /**
     * 操作
     */
    private String operation;
}
