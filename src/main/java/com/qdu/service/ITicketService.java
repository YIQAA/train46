package com.qdu.service;


import com.qdu.dto.domain.TicketListDTO;
import com.qdu.dto.req.TicketPageQueryReqDTO;
import com.qdu.dto.resp.ticketList.TicketPageQueryRespDTO;

import java.time.LocalDate;
import java.util.List;


public interface ITicketService {

    List<TicketListDTO> listTicketQuery(String fromCityCode, String toCityCode, LocalDate departureDate);


    TicketPageQueryRespDTO listTicketPageQuery(TicketPageQueryReqDTO ticketPageQueryReqDTO);
}
