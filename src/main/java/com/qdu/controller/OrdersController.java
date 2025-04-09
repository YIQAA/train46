package com.qdu.controller;

import com.qdu.common.page.PageResponse;
import com.qdu.dto.req.order.TicketOrderCreateReqDTO;
import com.qdu.dto.req.order.TicketOrderPageQueryReqDTO;
import com.qdu.dto.resp.order.TicketOrderDetailRespDTO;
import com.qdu.dto.resp.ticketList.UserPassengerRespDTO;
import com.qdu.service.impl.OrdersServiceImpl;
import com.qdu.service.impl.UsersServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 订单信息表 前端控制器
 * </p>
 *
 * @author 徐增润
 * @since 2025-03-14
 */
@RestController
@RequestMapping("/api/order-service")

public class OrdersController {

    @Autowired
    private OrdersServiceImpl orderService;
    @Autowired
    private UsersServiceImpl userService;


    //创建订单
    @PostMapping("/order/create")
    public String createTicketOrder(@RequestBody TicketOrderCreateReqDTO requestParam) {
        System.out.println("**************************************创建订单*********************************");
        System.out.println(requestParam);
        return orderService.createTicketOrder(requestParam);
    }

    /**
     * 根据订单号查询车票订单
     */
    @GetMapping("/order/queryBySn")
    public TicketOrderDetailRespDTO queryTicketOrderByOrderSn(@RequestParam(value = "orderSn") String orderSn) {
        System.out.println("**************************************根据订单号查询车票订单*********************************");
        return orderService.queryTicketOrderByOrderSn(orderSn);
    }

    /**
     * 分页查询车票订单
     */
    @GetMapping("/order/ticket/page")
    public PageResponse<TicketOrderDetailRespDTO> pageTicketOrder(TicketOrderPageQueryReqDTO requestParam) {
        System.out.println("**************************************分页查询车票订单*********************************");
        System.out.println(requestParam);
        return orderService.pageTicketOrderList(requestParam);
    }
    /**
     * 取消订单
     */
    @PostMapping("/order/ticket/cancel")
    public Boolean cancelTickOrder(@RequestParam (value = "orderSn") String orderSn) {
        System.out.println("**************************************取消订单*********************************");
        System.out.println(orderSn);
        return orderService.cancelOrder(orderSn);
    }


    //获取userpassenger信息
    @RequestMapping("/user/passenger")
    public UserPassengerRespDTO getUserPassenger(@RequestParam (value = "userName") String userName) {
        System.out.println("**************************************获取userpassenger信息="+userName+"*********************************");
        return userService.getUserPassenger(userName);
    }

}
