package com.qdu.service.impl;

import com.qdu.dto.domain.TicketListDTO;
import com.qdu.dto.domain.TrainSeatsDTO;
import com.qdu.dto.resp.AIchat.AiQueryTicketListDTO;
import com.qdu.mapper.StationMapper;
import com.qdu.service.IAIChatService;
import com.qdu.service.IStationsService;
import com.qdu.service.ITicketService;
import com.qdu.service.ITrainsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AIChatService implements IAIChatService {
    private final ITicketService ticketService;
    private final StationMapper stationMapper;

    //根据输入的出发城市、目的城市、出发日期，返回车票信息
    @Override
    public List<AiQueryTicketListDTO> listTicketQueryForAIService(String fromCity, String toCity, LocalDate departureDate) {

        System.out.println("+++++++++++++++++++++++++++++listTicketQueryForAIService调用 ++++++++++++++++++++++++++++++");
        System.out.println(fromCity+"  "+toCity+"  "+departureDate);

        String fromCityCode = stationMapper.getCityCodeByName(fromCity);
        String toCityCode = stationMapper.getCityCodeByName(toCity);

        //查询车票信息
        List<TicketListDTO> ticketList = new ArrayList<>();
        ticketList = ticketService.listTicketQuery(fromCityCode, toCityCode, departureDate);

        //封装aiQueryTicketListDTOS
        List<AiQueryTicketListDTO> aiQueryTicketListDTOS = new ArrayList<>();
        for (TicketListDTO ticket : ticketList) {
            AiQueryTicketListDTO aiQueryTicket = new AiQueryTicketListDTO();
            aiQueryTicket.setTrainId(ticket.getTrainId());
            aiQueryTicket.setTrainNumber(ticket.getTrainNumber());
            aiQueryTicket.setDepartureArrivalStation(ticket.getDepartureStation()+"-->"+ticket.getArrivalStation());
            aiQueryTicket.setDepartureArrivalTime(ticket.getDepartureTime()+" - "+ticket.getArrivalTime());
            aiQueryTicket.setDuration(ticket.getDuration());
            //余票状态及票价：充足、紧张、无票，xx元起
            Integer availableSeats = 0;
            Integer price = 9999999;
            for (TrainSeatsDTO seat : ticket.getSeatClassList()) {
                availableSeats+=seat.getQuantity();
                if (seat.getPrice()<price){
                    price = seat.getPrice();
                }
            }
            if (availableSeats>100){
                aiQueryTicket.setTicketAvailPrice("充足，"+price+"元起");
            }else if (availableSeats==0){
                aiQueryTicket.setTicketAvailPrice("无票");
            }else {
                aiQueryTicket.setTicketAvailPrice("紧张，"+price+"元起");
            }
            aiQueryTicketListDTOS.add(aiQueryTicket);
            //操作
            //http://localhost:5173/buyTicket?
            //  fromCity=北京&
            //  toCity=上海&
            //  departureDate=2024-03-20&
            //  trainNumber=G123&
            //  trainId=123456&
            //  departureStation=北京南站&
            //  arrivalStation=上海虹桥
            String operation = "http://localhost:5173/buyTicket?fromCity="+fromCityCode+"&toCity="+toCityCode+
                    "&departureDate="+departureDate+"&trainNumber="+ticket.getTrainNumber()+
                    "&trainId="+ticket.getTrainId()+"&departureStation="+ticket.getDepartureStation()+
                    "&arrivalStation="+ticket.getArrivalStation()+"";
            System.out.println("@@@@@@@@@@@@@@@@操作："+operation);
            aiQueryTicket.setOperation(operation);

        }

        return aiQueryTicketListDTOS;
    }




}
