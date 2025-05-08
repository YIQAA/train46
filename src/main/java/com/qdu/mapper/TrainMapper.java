package com.qdu.mapper;

import com.qdu.dto.domain.StationToStationRouteDTO;
import com.qdu.dto.resp.admin.TrainOccupancyDTO;
import com.qdu.dto.resp.ticketList.TrainStationQueryRespDTO;
import com.qdu.entity.Train;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 车次基础信息表 Mapper 接口
 * </p>
 *
 * @author 徐增润
 * @since 2025-03-23
 */
public interface TrainMapper extends BaseMapper<Train> {

    List<String> getTrainNumbersBetweenCities(@Param("departureCity") String departureCity, @Param("arrivalCity") String arrivalCity);


    Train getTrainByNumber(String trainNumber);

    StationToStationRouteDTO findTrainsByCities(@Param("depStationId") Long depStation, @Param("arrStationId") Long arrStation, @Param("date") LocalDate date);

    // 根据两个城市代码查询车次信息
    List<StationToStationRouteDTO> findTrainsByCityCodes(@Param("depCityCode") String depCityCode, @Param("arrCityCode") String arrCityCode, @Param("date") LocalDate date);

    String getTrainIdByNumberAndDate(@Param("trainNumber") String trainNumber,@Param("departureDate") LocalDate departureDate);

    StationToStationRouteDTO findTrainInfoByTrainId(@Param("trainId") Integer trainId,
                                                    @Param("startStationId") Integer startStationId,
                                                    @Param("endStationId") Integer endStationId);

    LocalDate findDepartureDateByTrainId(Integer trainId);

    List<String> getAllTrainNumber();

    List<TrainStationQueryRespDTO> getTrainStationByTrainNumber(String trainNumber);


    List<Map<String, Object>> getSimpleTrainOccupancy();
}
