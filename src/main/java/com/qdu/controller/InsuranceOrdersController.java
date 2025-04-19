package com.qdu.controller;

import com.qdu.common.convention.result.Result;

import com.qdu.common.convention.result.Results;
import com.qdu.dto.req.user.InsuranceCreateReqDTO;
import com.qdu.dto.resp.user.InsuranceListRespDTO;
import com.qdu.service.IInsuranceOrdersService;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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

    @GetMapping("/insurance/query")
    public Result<List<InsuranceListRespDTO>> queryTicketOrder(@RequestParam("userId")  Integer userId) {
        System.out.println("**************************************查询保险*********************************");

        return Results.success(insuranceOrdersService.queryInsuranceOrder(userId));
    }

}
