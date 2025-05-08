package com.qdu.dto.resp.admin;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public  class TrainOccupancyDTO {
    private String trainNumber;
    private BigDecimal occupancyRate;
}