package com.qdu.dto.req.admin;


import lombok.Data;

@Data
public class ToggleFreezeUserReqDTO {

    private Integer userId;
    private Boolean freeze;
}
