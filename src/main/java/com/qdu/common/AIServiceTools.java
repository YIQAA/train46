package com.qdu.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.qdu.dto.domain.TicketListDTO;
import com.qdu.dto.resp.AIchat.AiQueryTicketListDTO;
import com.qdu.dto.resp.ticketList.TicketPageQueryRespDTO;
import com.qdu.service.impl.AIChatService;
import com.qdu.service.impl.TicketServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.context.FunctionRegistration;
import org.springframework.cloud.function.context.catalog.FunctionTypeUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.function.Function;



@Configuration
public class AIServiceTools {

	@Autowired
	TicketServiceImpl ticketService;
	@Autowired
	AIChatService aiChatService;


	@Data
	@Description("车票查询参数")
	public static class TicketQueryParams {

		private String departureCity;


		private String destinationCity;


		private LocalDate departureDate;


	}
	@Data
	@AllArgsConstructor
	@Description("车票查询结果")
	public static class TicketQueryResult {
//		private List<TicketListDTO> tickets;
//
//		private int currentPage;
//
//		private int totalPages;

		private int code;
		private List<AiQueryTicketListDTO> tickets;
		private String errorMsg;
	}


	@Bean
	@Description("分页查询车票信息函数")
	public Function<TicketQueryParams, TicketQueryResult> listTicketPageQueryForAI() {
		System.out.println("+++++++++++++++++++++++++++++BeanForAI   1++++++++++++++++++++++++++++++");
		return params -> {
			try {
				// 参数校验
				if (params.getDepartureCity() == null || params.getDestinationCity() == null) {
					return new TicketQueryResult(400, null, "出发地和目的地不能为空");
				}
				System.out.println("+++++++++++++++++++++++++++++BeanForAI   2++++++++++++++++++++++++++++++"+params.getDepartureDate());
				// 调用业务Service
				List<AiQueryTicketListDTO> tickets = aiChatService.listTicketQueryForAIService(
						params.getDepartureCity(),
						params.getDestinationCity(),
						params.getDepartureDate()
				);
				return new TicketQueryResult(0, tickets, null);
			} catch (Exception e) {
				return new TicketQueryResult(500, null, "查询失败: " + e.getMessage());
			}
		};
	}



//	@Bean
//	@Description("查询车票")
//	public Function<checkTicketRequest, List<TicketListDTO>> listTicketPageQueryForAI() {
//		System.out.println("+++++++++++++++++++++++++++++ai的req++++++++++++++++++++++++++++++");
//		System.out.println(checkTicketRequest.class);
//		return request -> {
//			try {
//				return ticketService.listTicketPageQueryForAI(request.fromCity(), request.toCity(), request.departureDate());
//			}
//			catch (Exception e) {
//				System.out.println("+++++++++++++++++++++++++++++ai的错误++++++++++++++++++++++++++++++");
//				return null;
//			}
//		};
//	}

}
