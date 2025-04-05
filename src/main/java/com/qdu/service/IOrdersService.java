package com.qdu.service;

import com.qdu.dto.req.order.TicketOrderCreateReqDTO;
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

    public TicketOrderDetailRespDTO queryTicketOrderByOrderSn(String orderSn);

    //创建订单
    public String createTicketOrder(TicketOrderCreateReqDTO requestParam);

}
