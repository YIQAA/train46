package com.qdu.dto.resp.admin;

import lombok.Data;

@Data
public class UserListRespDTO {
    private Integer userid;
    private String username;
    private String phone;
    private Integer orderAmount;
    private Boolean frozen;


}
