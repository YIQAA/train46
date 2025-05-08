package com.qdu.mapper;

import com.qdu.dto.resp.admin.StationFlowDTO;
import com.qdu.dto.resp.ticketList.CityQueryRespDTO;
import com.qdu.dto.resp.ticketList.TrainStationQueryRespDTO;
import com.qdu.entity.Station;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 车站信息表 Mapper 接口
 * </p>
 *
 * @author 徐增润
 * @since 2025-03-23
 */
public interface StationMapper extends BaseMapper<Station> {


    String getStationNameByid(Integer stationId);

    Integer getStationIdByName(String stationName);

    List<Integer> getStationIdByCity(String city);


    List<CityQueryRespDTO> selectCityList();

    List<String> getStationNamesByCityCode(String code);

    List<Long> findStationIdsByCityCode(String code);

    Integer getStationSeqByTrainIdAndStationName(@Param("trainNumber") String trainNumber,@Param("stationName") String stationName);

    //获取车次信息
    List<TrainStationQueryRespDTO> getTrainStationByTrainId(String trainId);


    String getCityCodeByName(String fromCity);

    List<StationFlowDTO> getStationTop5Flow();
}
