package com.qdu.service;


import com.qdu.dto.domain.TicketListDTO;
import com.qdu.dto.req.TicketPageQueryReqDTO;
import com.qdu.dto.resp.ticketList.TicketPageQueryRespDTO;

import java.time.LocalDate;
import java.util.List;


public interface ITicketService {


    //根据城市code和日期，查询车票信息
    List<TicketListDTO> listTicketQuery(String fromCityCode, String toCityCode, LocalDate departureDate);

    //余票查询页面，分页查询车票信息
    TicketPageQueryRespDTO listTicketPageQuery(TicketPageQueryReqDTO ticketPageQueryReqDTO);

    //取消订单
    boolean cancelOrder(String orderSn);
}
