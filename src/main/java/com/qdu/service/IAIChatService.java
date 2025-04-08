package com.qdu.service;

import com.qdu.dto.resp.AIchat.AiQueryTicketListDTO;

import java.time.LocalDate;
import java.util.List;

public interface IAIChatService {

    List<AiQueryTicketListDTO> listTicketQueryForAIService(String fromCity, String toCity, LocalDate departureDate);
}
