package com.qdu.service.impl;

import com.qdu.dto.domain.StationToStationRouteDTO;
import com.qdu.entity.Train;
import com.qdu.mapper.DailyTrainMapper;
import com.qdu.mapper.TrainMapper;
import com.qdu.service.ITrainsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 每日车次信息表 服务实现类
 * </p>
 *
 * @author 徐增润
 * @since 2025-03-14
 */
@Service
@AllArgsConstructor
public class TrainsServiceImpl extends ServiceImpl<TrainMapper, Train> implements ITrainsService {

    private final TrainMapper trainMapper;
    private final DailyTrainMapper dailyTrainMapper;

    @Override
    public List<StationToStationRouteDTO> findTrainsByCityCodes(String depCityCode, String arrCityCode, LocalDate date) {
        return trainMapper.findTrainsByCityCodes(depCityCode,arrCityCode,date);
    }

    @Override
    public String getTrainIdByNumberAndDate(String trainNumber, LocalDate departureDate) {
        return trainMapper.getTrainIdByNumberAndDate(trainNumber,departureDate);
    }

    @Override
    public StationToStationRouteDTO findTrainInfoByTrainId(Integer trainId, Integer startStationId, Integer endStationId) {
        return trainMapper.findTrainInfoByTrainId(trainId,startStationId,endStationId);
    }

    @Override
    public LocalDate findDepartureDateByTrainId(Integer trainId) {
        return baseMapper.findDepartureDateByTrainId(trainId);
    }


}
