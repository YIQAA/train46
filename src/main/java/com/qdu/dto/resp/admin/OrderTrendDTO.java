package com.qdu.dto.resp.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public  class OrderTrendDTO {
    private String date;
    private Integer count;
}
