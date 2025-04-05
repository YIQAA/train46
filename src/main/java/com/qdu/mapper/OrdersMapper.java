package com.qdu.mapper;

import com.qdu.entity.OrderPassengers;
import com.qdu.entity.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

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
}
