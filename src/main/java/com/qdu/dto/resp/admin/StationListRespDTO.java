package com.qdu.dto.resp.admin;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StationListRespDTO {

    private String stationId;
    private String stationName;
    private String city;
    private String cityCode;
}
