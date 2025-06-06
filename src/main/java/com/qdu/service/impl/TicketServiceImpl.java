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
import com.qdu.service.ISeatService;
import com.qdu.service.ITrainsService;
import com.qdu.service.IStationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements ITicketService {

    private final ITrainsService trainService;
    private final IStationsService stationService;
    private final ObjectMapper objectMapper;
    private final TrainMapper trainMapper;
    private final StationMapper stationMapper;
    private final ISeatService seatService;
    private final RedisTemplate<String, Object> redisTemplate;

    //根据出发城市code，到达城市code，日期  查询车次车票信息列表
    public List<TicketListDTO> listTicketQuery(String fromCityCode, String toCityCode, LocalDate departureDate) {
        String key = "ticket:query:" + fromCityCode + ":" + toCityCode + ":" + departureDate;
        List<TicketListDTO> dtos = (List<TicketListDTO>) redisTemplate.opsForValue().get(key);
        System.out.println("redis查询车票信息列表");
        if (dtos == null) {
            dtos = new ArrayList<>();
            System.out.println("redis失效，查询车票信息列表");
            List<StationToStationRouteDTO> routes = trainService.findTrainsByCityCodes(fromCityCode, toCityCode, departureDate);
            for (StationToStationRouteDTO route : routes) {
                TicketListDTO dto = new TicketListDTO();
                String trainId = trainService.getTrainIdByNumberAndDate(route.getTrainNumber(), departureDate);
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
                List<TrainSeatsDTO> seats = seatService.selectAvailableSeats(trainId, route.getDistance());
                dto.setSeatClassList(seats);
                dtos.add(dto);
            }
            redisTemplate.opsForValue().set(key, dtos, 30, TimeUnit.MINUTES);
        }
        // 过滤当天已过期的车次
        LocalDateTime now = LocalDateTime.now();
        List<TicketListDTO> filteredList = new ArrayList<>();
        for (TicketListDTO dto : dtos) {
            try {
                LocalTime departureTime = LocalTime.parse(dto.getDepartureTime() + ":00");
                LocalDateTime departureDateTime = departureDate.atTime(departureTime);
                if (!departureDate.equals(now.toLocalDate()) || departureDateTime.isAfter(now)) {
                    filteredList.add(dto);
                }
            } catch (DateTimeParseException e) {
                System.err.println("解析出发时间失败：" + dto.getDepartureTime());
            }
        }
        return filteredList;
    }

    //余票查询页面     查询车票信息
    public TicketPageQueryRespDTO listTicketPageQuery(TicketPageQueryReqDTO requestParam) {
        String key = "ticket:pageQuery:" + requestParam.getFromCity() + ":" + requestParam.getToCity() + ":" + requestParam.getDepartureDate();
        TicketPageQueryRespDTO resp = (TicketPageQueryRespDTO) redisTemplate.opsForValue().get(key);
        if (resp == null) {
            resp = new TicketPageQueryRespDTO();

            // 1. 获取城市对应的所有车站
            resp.setDepartureStationList(stationService.getStationNamesByCityCode(requestParam.getFromCity()));
            resp.setArrivalStationList(stationService.getStationNamesByCityCode(requestParam.getToCity()));

            List<TicketListDTO> ticketdtos = listTicketQuery(requestParam.getFromCity(), requestParam.getToCity(), requestParam.getDepartureDate());
            resp.setTrainList(ticketdtos);

            // 将查询结果存入 Redis 缓存，设置缓存时间为 30 分钟
            redisTemplate.opsForValue().set(key, resp, 30, TimeUnit.MINUTES);
        }
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