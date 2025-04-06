package com.qdu.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qdu.dto.domain.TicketListDTO;
import com.qdu.dto.domain.TrainInfo;
import com.qdu.dto.domain.TrainSeatsDTO;
import com.qdu.dto.req.TicketPageQueryReqDTO;
import com.qdu.dto.resp.ticketList.TicketPageQueryRespDTO;

import com.qdu.mapper.StationMapper;
import com.qdu.mapper.TrainMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;

import java.util.List;
import java.util.Locale;


@Service
@RequiredArgsConstructor
public class TicketServiceImpl {

    private final TrainsServiceImpl trainService;
    private final StationsServiceImpl stationService;
    private final ObjectMapper objectMapper;
    private final TrainMapper trainMapper;
    private final StationMapper stationMapper;
    private final SeatServiceImpl seatService;

    //根据三个请求参数查询车票信息
    public TicketPageQueryRespDTO listTicketPageQuery(TicketPageQueryReqDTO requestParam) {

        TicketPageQueryRespDTO resp = new TicketPageQueryRespDTO();

        // 1. 获取城市对应的所有车站
        resp.setDepartureStationList(stationService.getStationNamesByCityCode(requestParam.getFromCity()));
        resp.setArrivalStationList(stationService.getStationNamesByCityCode(requestParam.getToCity()));

        LocalDate departureDate =  requestParam.getDepartureDate();

        // 2. 根据城市code和日期查询车次基础信息
        List<TrainInfo> trains=trainService.findTrainsByCityCodes(requestParam.getFromCity(), requestParam.getToCity(), departureDate);
        System.out.println("------------------------------------车次信息------------------------------------");
        System.out.println(trains);


        List<TicketListDTO> dtos = new ArrayList<>();
        for (TrainInfo train : trains) {
            TicketListDTO dto = new TicketListDTO();
            String trainId = trainService.getTrainIdByNumberAndDate(train.getTrainNumber(), departureDate);
            dto.setTrainId(trainId);
            // 基础信息
            dto.setTrainNumber(train.getTrainNumber());
            dto.setTrainType(train.getTrainType());
            dto.setDepartureStation(train.getDepartureStation());
            dto.setArrivalStation(train.getArrivalStation());
            dto.setDepartureTime(train.getDepartureTime().toString().replaceAll(":\\d+$", ""));
            dto.setArrivalTime(train.getArrivalTime().toString().replaceAll(":\\d+$", ""));
            dto.setDuration(convertMinutesToHourMinute(train.getDuration()));
            dto.setDepartureFlag(train.getDepartureFlag());
            dto.setArrivalFlag(train.getArrivalFlag());

            //根据车次和车站名称查站序
            Integer startSeq = stationService.getStationSeqByTrainIdAndStationName(train.getTrainNumber(), train.getDepartureStation());
            Integer endSeq = stationService.getStationSeqByTrainIdAndStationName(train.getTrainNumber(), train.getArrivalStation());

            // 3. 查询座位信息
            List<TrainSeatsDTO> seats = seatService.selectAvailableSeats(trainId, startSeq, endSeq, train.getDistance());
            dto.setSeatClassList(seats);
            System.out.println("------------------------------------座位配置------------------------------------");
            System.out.println(seats);
            dtos.add(dto);
        }
        resp.setTrainList(dtos);

        System.out.println("-------------------------总的resp-------------------------------");
        System.out.println(resp);
        return resp;

    }

    //ai适用方法，根据城市名和日期查询
    public List<TicketListDTO> listTicketPageQueryForAIService(String fromCity, String toCity, LocalDate departureDate) {
        List<TicketListDTO> dtos = new ArrayList<>();
        System.out.println("+++++++++++++++++++++++++++++ai的service ++++++++++++++++++++++++++++++");
        System.out.println(fromCity+"  "+toCity+"  "+departureDate);
        String fromCityCode = stationMapper.getCityCodeByName(fromCity);
        String toCityCode = stationMapper.getCityCodeByName(toCity);
        TicketPageQueryReqDTO requestParam = new TicketPageQueryReqDTO();
        requestParam.setFromCity(fromCityCode);
        requestParam.setToCity(toCityCode);
        requestParam.setDepartureDate(departureDate);

        dtos = listTicketPageQuery(requestParam).getTrainList();

        System.out.println("+++++++++++++++++++++++++++++ai的service的resp++++++++++++++++++++++++++++++");
        System.out.println(requestParam);
        System.out.println(dtos);
        return dtos;
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