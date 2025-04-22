package com.qdu.service;

import com.qdu.dto.req.RefundTicketReqDTO;
import com.qdu.dto.req.order.TicketOrderCreateReqDTO;
import com.qdu.dto.resp.user.RefundTicketRespDTO;
import com.qdu.dto.resp.order.TicketOrderDetailRespDTO;
import com.qdu.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单主表 服务类
 * </p>
 *
 * @author 徐增润
 * @since 2025-03-28
 */
public interface IOrdersService extends IService<Orders> {

    //根据订单号，查询订单详情
    public TicketOrderDetailRespDTO queryTicketOrderByOrderSn(String orderSn);

    //创建订单
    public String createTicketOrder(TicketOrderCreateReqDTO requestParam);

    //取消订单
    public Boolean cancelOrder(String orderSn);

    //根据订单号查询订单状态
    public Integer getOrderStatusByOrderSn(String orderSn);

    //根据订单号支付
    public Boolean pay(String orderSn);

    //根据订单号退款

    RefundTicketRespDTO commonTicketRefund(RefundTicketReqDTO requestParam);
}
