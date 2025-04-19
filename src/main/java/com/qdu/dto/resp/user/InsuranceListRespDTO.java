package com.qdu.dto.resp.user;

import lombok.Data;

import java.time.LocalDate;

@Data
public class InsuranceListRespDTO {

    // 保险类型
    private Integer insuranceType;

    private String insuredName;

    private String insuredIdCard;


    private String policyHolderName;

    private String policyHolderIdCard;


    private String period;

    private String status;
}
