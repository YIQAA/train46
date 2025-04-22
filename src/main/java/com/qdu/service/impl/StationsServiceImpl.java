package com.qdu.service.impl;

import com.qdu.dto.req.admin.StationCreateRepDTO;
import com.qdu.dto.resp.admin.StationListRespDTO;
import com.qdu.dto.resp.ticketList.CityQueryRespDTO;
import com.qdu.dto.resp.ticketList.TrainStationQueryRespDTO;
import com.qdu.entity.Station;
import com.qdu.mapper.StationMapper;

import com.qdu.service.IStationsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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



    @Autowired
    private StationMapper stationMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public List<CityQueryRespDTO> listAllStationWithCache() {
        System.out.println("查询所有城市");
        String key = "all_cities";
        List<CityQueryRespDTO> cities = (List<CityQueryRespDTO>) redisTemplate.opsForValue().get(key);
        if (cities == null) {
            System.out.println("redis失效，查询所有城市");
            cities = stationMapper.selectCityList();
            // 将查询结果存入 Redis 缓存，设置缓存时间为 1 小时
            redisTemplate.opsForValue().set(key, cities, 1, TimeUnit.HOURS);
        }
        return cities;
    }







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
    @Override
    public List<StationListRespDTO> getAdminAllStations() {

        List<Station> stationList = baseMapper.selectList(null);
        List<StationListRespDTO> stationListRespDTOS = new ArrayList<>();
        for (Station station : stationList) {
            StationListRespDTO stationListRespDTO = new StationListRespDTO();
            stationListRespDTO.setStationId(String.valueOf(station.getStationId()));
            stationListRespDTO.setStationName(station.getStationName());
            stationListRespDTO.setCity(station.getCity());
            stationListRespDTO.setCityCode(station.getCityCode());
            stationListRespDTOS.add(stationListRespDTO);
        }
        return stationListRespDTOS;
    }

    @Override
    public Boolean createStation(StationCreateRepDTO stationCreateRepDTO) {
        Station station = new Station();
        station.setStationName(stationCreateRepDTO.getStationName());
        station.setCity(stationCreateRepDTO.getCityName());
        station.setCityCode(stationCreateRepDTO.getCityCode());
        System.out.println("车站"+station.getStationName()+"创建成功"+station.getCity()+"0000"+station.getCityCode());
        baseMapper.insert(station);
        return true;
    }


}
