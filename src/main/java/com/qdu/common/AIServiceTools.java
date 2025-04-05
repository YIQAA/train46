package com.qdu.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.qdu.dto.domain.TicketListDTO;
import com.qdu.dto.resp.ticketList.TicketPageQueryRespDTO;
import com.qdu.service.impl.TicketServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Configuration
public class AIServiceTools {

	@Autowired
	TicketServiceImpl ticketService;


	public record checkTicketRequest(String fromCity,String toCity,Date departureDate){}

	@Bean
	@Description("查询车票")
	public Function<checkTicketRequest, List<TicketListDTO>> listTicketPageQueryForAI() {
		return request -> {
			try {
				return ticketService.listTicketPageQueryForAI(request.fromCity(), request.toCity(), request.departureDate());
			}
			catch (Exception e) {

				return null;
			}
		};
	}

}
