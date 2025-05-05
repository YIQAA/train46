package com.qdu.dto.resp.admin;

import com.qdu.dto.resp.ticketList.TrainStationQueryRespDTO;
import lombok.Data;

import java.util.List;

@Data
public class TrainListRespDTO {

    private String trainNumber;
    private String trainType;
    private String trainStartStation;
    private String trainEndStation;
    private String trainTotalTime;
    private String trainDistance;
    List<TrainStationQueryRespDTO> trainStationQueryRespDTOList;
}
