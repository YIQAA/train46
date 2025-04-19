package com.qdu.dto.resp.user;

import lombok.Data;

@Data
public class UserQueryRespDTO {

    private String username;
    private String realName;
    private Integer idType;
    private String idCard;
    private String phone;
    private String status;
}
