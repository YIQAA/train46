package com.qdu.service;

import com.qdu.common.page.PageResponse;
import com.qdu.dto.req.order.TicketOrderPageQueryReqDTO;
import com.qdu.dto.resp.admin.AdminIndexRespDTO;
import com.qdu.dto.resp.order.TicketOrderDetailRespDTO;

public interface IAdminService {

    //获取订单列表
    public PageResponse<TicketOrderDetailRespDTO> adminTicketOrderList(TicketOrderPageQueryReqDTO requestParam);

    public AdminIndexRespDTO getIndexData();
}
