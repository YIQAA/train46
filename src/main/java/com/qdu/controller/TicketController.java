package com.qdu.controller;

import com.qdu.dto.req.TicketPageQueryReqDTO;
import com.qdu.dto.resp.ticketList.CityQueryRespDTO;
import com.qdu.dto.resp.ticketList.TicketPageQueryRespDTO;
import com.qdu.dto.resp.ticketList.TrainStationQueryRespDTO;
import com.qdu.mapper.StationMapper;
import com.qdu.service.impl.StationsServiceImpl;
import com.qdu.service.impl.TicketServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 车票信息表 前端控制器
 * </p>
 *
 * @author 徐增润
 * @since 2025-03-16
 */
@RestController
@RequestMapping("/api/ticket-service")
public class TicketController {

    @Autowired
    private TicketServiceImpl ticketService;
    @Autowired
    private StationMapper stationMapper;
    @Autowired
    private StationsServiceImpl stationsService;

    //查票列表
    @GetMapping("/ticketQueryList")
    public TicketPageQueryRespDTO pageListTicketQuery(TicketPageQueryReqDTO requestParam) {

        System.out.println("***************************查票列表，控制器启动****************************");
        System.out.println(stationMapper.selectCityList());

        System.out.println(requestParam);
        return ticketService.listTicketPageQuery(requestParam);
    }
    //获取所有城市
    @GetMapping("/city/all")
    public List<CityQueryRespDTO> listAllStation() {
        return stationsService.listAllStationWithCache();
    }

    //根据trainid查询该车次的所有经过站
    @GetMapping("/train-station/query")
    public List<TrainStationQueryRespDTO> listTrainStationQuery(@RequestParam ("trainId") String trainId) {
        System.out.println("-----------------根据trainid查询该车次的所有经过站-------------------------");
        System.out.println(stationMapper.getTrainStationByTrainId(trainId));
        return stationMapper.getTrainStationByTrainId(trainId);
    }




}
