package com.qdu.dto.resp.ticketList;

import lombok.Data;

@Data
public class UserPassengerRespDTO {
    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 身份证号
     */
    private String idCard;
}
