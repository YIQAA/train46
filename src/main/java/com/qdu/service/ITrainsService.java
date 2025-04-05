package com.qdu.service;

import com.qdu.dto.domain.TrainInfo;
import com.qdu.entity.DailyTrain;
import com.qdu.entity.Train;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.Data;

import java.util.Date;
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
    List<TrainInfo> findTrainsByCityCodes(String depCityCode,String arrCityCode, Date date);
    //根据车次和日期查trainid
    String getTrainIdByNumberAndDate(String trainNumber, Date departureDate);

    //OrdersController
    //根据trainId、出发车站、到达车站查询车次信息
    TrainInfo findTrainInfoByTrainId(Integer trainId, Integer startStationId, Integer endStationId);

    //根据trainId查询出发日期
    Date findDepartureDateByTrainId(Integer trainId);

}
