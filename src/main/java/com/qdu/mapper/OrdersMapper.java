package com.qdu.mapper;

import com.qdu.dto.resp.admin.OrderTrendDTO;
import com.qdu.entity.OrderPassengers;
import com.qdu.dto.resp.admin.PassengerTrendDTO;
import com.qdu.entity.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qdu.service.impl.AdminServiceImpl;

import java.util.List;

/**
 * <p>
 * 订单信息表 Mapper 接口
 * </p>
 *
 * @author 徐增润
 * @since 2025-03-23
 */
public interface OrdersMapper extends BaseMapper<Orders> {

    Integer findOrderIdByOrderSn(String orderSn);


    List<String> selectOrderSnByUserId(String userId);

    //订单取消
    int updateOrderStatusToCanceled(String orderSn);
    int unlockSeatsByOrderSn(String orderSn);

    //支付
    int updateOrderStatusToPaid(String orderSn);
    int occupySeatsByOrderSn(String orderSn);


    // 查询订单状态
    Integer getOrderStatusByOrderSn(String orderSn);

    List<String> selectAllOrderSn(Object o);


    Integer getOrderCountByUserId(Integer userId);

    Integer getTodayOrderCount();

    Integer getTodayPassengerCount();

    List<PassengerTrendDTO> getPassengerTrend(int days);

    List<OrderTrendDTO> getOrderTrend(int days);
}
