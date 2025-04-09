package com.qdu.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qdu.dto.domain.TicketListDTO;
import com.qdu.dto.domain.StationToStationRouteDTO;
import com.qdu.dto.domain.TrainSeatsDTO;
import com.qdu.dto.req.TicketPageQueryReqDTO;
import com.qdu.dto.resp.ticketList.TicketPageQueryRespDTO;

import com.qdu.mapper.StationMapper;
import com.qdu.mapper.TrainMapper;

import com.qdu.service.ITicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements ITicketService {

    private final TrainsServiceImpl trainService;
    private final StationsServiceImpl stationService;
    private final ObjectMapper objectMapper;
    private final TrainMapper trainMapper;
    private final StationMapper stationMapper;
    private final SeatServiceImpl seatService;

    //根据出发城市code，到达城市code，日期  查询 车次车票信息列表
    public List<TicketListDTO> listTicketQuery(String fromCityCode, String toCityCode, LocalDate departureDate) {

        List<TicketListDTO> dtos = new ArrayList<>();

        //查询两城市之间的火车信息，因为一个城市可能有多个车站，一条火车路线中，可能包含一个城市的多个站点，
        // 比如北京南-上海虹桥，北京西-上海虹桥，所以根据两个城市名称可能会查出多条站到站的路线信息
        List<StationToStationRouteDTO> routes=trainService.findTrainsByCityCodes(fromCityCode, toCityCode, departureDate);

        for (StationToStationRouteDTO route : routes) {
            TicketListDTO dto = new TicketListDTO();
            // 1. 根据车次号和出发日期查询可能重复的 车次id，
            String trainId = trainService.getTrainIdByNumberAndDate(route.getTrainNumber(), departureDate);
            // 2. 基础信息封装到TicketListDTO
            dto.setTrainId(trainId);
            dto.setTrainNumber(route.getTrainNumber());
            dto.setTrainType(route.getTrainType());
            dto.setDepartureStation(route.getDepartureStation());
            dto.setArrivalStation(route.getArrivalStation());
            dto.setDepartureTime(route.getDepartureTime().toString().replaceAll(":\\d+$", ""));
            dto.setArrivalTime(route.getArrivalTime().toString().replaceAll(":\\d+$", ""));
            dto.setDuration(convertMinutesToHourMinute(route.getDuration()));
            dto.setDepartureFlag(route.getDepartureFlag());
            dto.setArrivalFlag(route.getArrivalFlag());
            // 3. 根据查到的具体 每日车次id 和 里程  查询  各种座位票价  信息
            List<TrainSeatsDTO> seats = seatService.selectAvailableSeats(trainId, route.getDistance());
            dto.setSeatClassList(seats);

            dtos.add(dto);
        }

        return dtos;
    }


    //余票查询页面     查询车票信息
    public TicketPageQueryRespDTO listTicketPageQuery(TicketPageQueryReqDTO requestParam) {

        TicketPageQueryRespDTO resp = new TicketPageQueryRespDTO();

        // 1. 获取城市对应的所有车站
        resp.setDepartureStationList(stationService.getStationNamesByCityCode(requestParam.getFromCity()));
        resp.setArrivalStationList(stationService.getStationNamesByCityCode(requestParam.getToCity()));

        List<TicketListDTO> ticketdtos = listTicketQuery(requestParam.getFromCity(), requestParam.getToCity(), requestParam.getDepartureDate());
        resp.setTrainList(ticketdtos);

        System.out.println("-------------------------余票查询页面---总的resp-------------------------------");
        System.out.println(resp);
        return resp;

    }

    @Override
    public boolean cancelOrder(String orderSn) {







        return false;
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