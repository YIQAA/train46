package com.qdu.controller;


import com.qdu.service.impl.OrdersServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 支付控制器
 * </p>
 *
 * @author 徐增润
 * @since 2025-03-14
 */
@RestController
@RequestMapping("/api/pay-service")
public class PayController {

    @Autowired
    private OrdersServiceImpl ordersService;

    /**
     * 公共支付接口
     * 对接常用支付方式，比如：支付宝、微信以及银行卡等
     */
    @PostMapping("/pay/create")
    public Boolean pay(@RequestParam(value = "orderSn") String orderSn) {

        return ordersService.pay(orderSn);
    }


    /**
     * 跟据订单号查询支付单详情
     */
    @GetMapping("/payStatus/query")
    public Integer getPayStatusByOrderSn(@RequestParam(value = "orderSn") String orderSn) {
        return ordersService.getOrderStatusByOrderSn(orderSn);
    }


}
