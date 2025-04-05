package com.qdu.service;


import com.qdu.dto.req.TicketPageQueryReqDTO;
import com.qdu.dto.resp.ticketList.TicketPageQueryRespDTO;



public interface ITicketService {
    TicketPageQueryRespDTO listTicketPageQuery(TicketPageQueryReqDTO ticketPageQueryReqDTO);
}
