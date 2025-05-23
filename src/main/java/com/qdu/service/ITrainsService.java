package com.qdu.service;

import com.qdu.dto.domain.StationToStationRouteDTO;
import com.qdu.dto.resp.admin.TrainListRespDTO;
import com.qdu.entity.Train;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 每日车次信息表 服务类
 * </p>
 *
 * @author 徐增润
 * @since 2025-03-14
 */
public interface ITrainsService extends IService<Train> {


    //TicketSearchController
    //根据两城市查询车次信息
    List<StationToStationRouteDTO> findTrainsByCityCodes(String depCityCode, String arrCityCode, LocalDate date);
    //根据车次和日期查trainid
    String getTrainIdByNumberAndDate(String trainNumber, LocalDate departureDate);

    //OrdersController
    //根据trainId、出发车站、到达车站查询车次信息
    StationToStationRouteDTO findTrainInfoByTrainId(Integer trainId, Integer startStationId, Integer endStationId);

    //根据trainId查询出发日期
    LocalDate findDepartureDateByTrainId(Integer trainId);

    List<TrainListRespDTO> listTrainStationQuery();

}
