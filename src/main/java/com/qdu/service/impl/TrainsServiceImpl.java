package com.qdu.service.impl;

import com.qdu.dto.domain.TrainInfo;
import com.qdu.entity.DailyTrain;
import com.qdu.entity.Train;
import com.qdu.mapper.DailyTrainMapper;
import com.qdu.mapper.TrainMapper;
import com.qdu.service.ITrainsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.Date;
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
    public List<TrainInfo> findTrainsByCityCodes(String depCityCode, String arrCityCode, Date date) {
        return trainMapper.findTrainsByCityCodes(depCityCode,arrCityCode,date);
    }

    @Override
    public String getTrainIdByNumberAndDate(String trainNumber, Date departureDate) {
        return trainMapper.getTrainIdByNumberAndDate(trainNumber,departureDate);
    }

    @Override
    public TrainInfo findTrainInfoByTrainId(Integer trainId, Integer startStationId, Integer endStationId) {
        return trainMapper.findTrainInfoByTrainId(trainId,startStationId,endStationId);
    }

    @Override
    public Date findDepartureDateByTrainId(Integer trainId) {
        return baseMapper.findDepartureDateByTrainId(trainId);
    }


}
