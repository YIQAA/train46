package com.qdu.service.impl;

import com.qdu.dto.resp.ticketList.TrainStationQueryRespDTO;
import com.qdu.entity.Station;
import com.qdu.mapper.StationMapper;

import com.qdu.service.IStationsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 车站信息表 服务实现类
 * </p>
 *
 * @author 徐增润
 * @since 2025-03-14
 */
@Service
public class StationsServiceImpl extends ServiceImpl<StationMapper, Station> implements IStationsService {



    // 根据城市代码查询城市所有站点名称
    @Override
    public List<String> getStationNamesByCityCode(String Code) {
        return baseMapper.getStationNamesByCityCode(Code);
    }

    @Override
    public List<Long> findStationIdsByCityCode(String Code) {
        return baseMapper.findStationIdsByCityCode(Code);
    }

    @Override
    public Integer getStationSeqByTrainIdAndStationName(String trainNumber, String stationName) {
        return baseMapper.getStationSeqByTrainIdAndStationName(trainNumber, stationName);
    }

    // 根据站点名查询站点id
    @Override
    public Integer getStationIdByName(String stationName) {
        return baseMapper.getStationIdByName(stationName);
    }

    @Override
    public String getStationNameById(Integer stationId) {
        return baseMapper.getStationNameByid(stationId);
    }

    //根据trainid查询该车次的所有经过站
    List<TrainStationQueryRespDTO> getTrainStationByTrainId(String trainId) {
        return baseMapper.getTrainStationByTrainId(trainId);
    }



}
