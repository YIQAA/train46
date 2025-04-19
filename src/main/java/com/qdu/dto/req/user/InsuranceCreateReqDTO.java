package com.qdu.dto.req.user;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

/**
 * 保险注册请求参数
 */
@Data
public class InsuranceCreateReqDTO {
    private Integer userId;

    // 保险类型
    private Integer insuranceType;

    // 保险天数

    private Integer days;

    // 保险金额（单位：元）
    private Integer amount;

    // 被保险人信息
    private String insuredName;
    private String insuredIdCard;
    private String insuredPhone;

    // 投保人信息
    private String policyHolderName;
    private String policyHolderIdCard;
    private String policyHolderPhone;

    // 保险期限
    private LocalDate startTime;
    private LocalDate endTime;

}