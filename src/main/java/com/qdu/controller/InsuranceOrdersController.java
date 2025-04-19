package com.qdu.controller;

import com.qdu.dto.req.order.TicketOrderCreateReqDTO;
import com.qdu.dto.req.user.InsuranceCreateReqDTO;
import com.qdu.service.IInsuranceOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 保险订单表 前端控制器
 * </p>
 *
 * @author 徐增润
 * @since 2025-04-18
 */
@RestController
@RequestMapping("/api/insurance-service")
public class InsuranceOrdersController {

    @Autowired
    private IInsuranceOrdersService insuranceOrdersService;

    @PostMapping("/insurance/create")
    public String createTicketOrder(@RequestBody InsuranceCreateReqDTO requestParam) {
        System.out.println("**************************************创建保险*********************************");
        System.out.println(requestParam);
        return insuranceOrdersService.createInsuranceOrder(requestParam);
    }

}
