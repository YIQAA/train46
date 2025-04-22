package com.qdu.service;

import com.qdu.dto.req.admin.StationCreateRepDTO;
import com.qdu.dto.resp.admin.StationListRespDTO;
import com.qdu.entity.Station;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 车站信息表 服务类
 * </p>
 *
 * @author 徐增润
 * @since 2025-03-14
 */
public interface IStationsService extends IService<Station> {



    // 根据城市代码获取城市所有站点名称
    List<String> getStationNamesByCityCode(String Code);

    List<Long> findStationIdsByCityCode(String Code);

    //根据车次和车站名称查站序
    Integer getStationSeqByTrainIdAndStationName(String trainNumber, String stationName);

    //根据车站名称查车站id
    Integer getStationIdByName(String stationName);

    //根据车站id查车站名称
    String getStationNameById(Integer stationId);


    //获取所有站点信息
    List<StationListRespDTO> getAdminAllStations();

    //创建站点
    Boolean createStation(StationCreateRepDTO stationCreateRepDTO);

}
