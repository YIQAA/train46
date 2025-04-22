package com.qdu.service.impl;

import com.qdu.dto.domain.StationToStationRouteDTO;
import com.qdu.dto.resp.admin.TrainListRespDTO;
import com.qdu.dto.resp.ticketList.TrainStationQueryRespDTO;
import com.qdu.entity.Train;
import com.qdu.mapper.DailyTrainMapper;
import com.qdu.mapper.StationMapper;
import com.qdu.mapper.TrainMapper;
import com.qdu.service.ITrainsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
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
    private final StationsServiceImpl stationsService;
    private final StationMapper stationMapper;

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

    @Override
    public List<TrainListRespDTO> listTrainStationQuery() {

        List<String> trainNumbers = trainMapper.getAllTrainNumber();

        List<TrainListRespDTO> trainListRespDTOS = new ArrayList<>();
        for (String trainNumber : trainNumbers)
        {
            TrainListRespDTO trainListRespDTO = new TrainListRespDTO();
            List<TrainStationQueryRespDTO> trainStationQueryRespDTOList = trainMapper.getTrainStationByTrainNumber(trainNumber);
            Train train = trainMapper.getTrainByNumber(trainNumber);
            trainListRespDTO.setTrainNumber(trainNumber);
            trainListRespDTO.setTrainStartStation(stationMapper.getStationNameByid(train.getStartStationId()));
            trainListRespDTO.setTrainEndStation(stationMapper.getStationNameByid(train.getEndStationId()));
            trainListRespDTO.setTrainTotalTime(convertMinutesToHourMinute(train.getTotalTravelTime()) );
            trainListRespDTO.setTrainDistance(train.getTotalDistance()+"公里");
            trainListRespDTO.setTrainType(train.getTrainType());
            trainListRespDTO.setTrainStationQueryRespDTOList(trainStationQueryRespDTOList);
            trainListRespDTOS.add(trainListRespDTO);
        }
        return trainListRespDTOS;
    }



    // 将分钟数转换为小时分钟表示
    public String convertMinutesToHourMinute(int minutes) {
        if (minutes < 0) {
            throw new IllegalArgumentException("分钟数不能为负数");
        }
        int hours = minutes / 60;
        int remainingMinutes = minutes % 60;
        StringBuilder result = new StringBuilder();
        if (hours > 0) {
            result.append(hours).append("小时");
        }
        if (remainingMinutes > 0) {
            result.append(remainingMinutes).append("分钟");
        }
        // 处理分钟数为0的情况
        if (result.length() == 0) {
            return "0分钟";
        }
        return result.toString();
    }


}
