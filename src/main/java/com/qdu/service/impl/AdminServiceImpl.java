package com.qdu.service.impl;

import com.qdu.common.page.PageResponse;
import com.qdu.dto.req.order.TicketOrderPageQueryReqDTO;
import com.qdu.dto.resp.admin.AdminIndexRespDTO;
import com.qdu.dto.resp.order.TicketOrderDetailRespDTO;
import com.qdu.mapper.OrdersMapper;
import com.qdu.service.IAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements IAdminService {

    private final OrdersMapper ordersMapper;
    private final OrdersServiceImpl ordersService;



    @Override
    public PageResponse<TicketOrderDetailRespDTO> adminTicketOrderList(TicketOrderPageQueryReqDTO requestParam) {


        List<String> orderSnList =new ArrayList<>();
        if (requestParam.getUserId() == null){
            orderSnList = ordersMapper.selectAllOrderSn(null);
        }else {
            //根据userid查所有orderSn
            orderSnList = ordersMapper.selectOrderSnByUserId(requestParam.getUserId());
        }


        PageResponse<TicketOrderDetailRespDTO> pageResponse = PageResponse.<TicketOrderDetailRespDTO>builder()
                .current(requestParam.getCurrent())
                .size(requestParam.getSize())
                .records(new ArrayList<TicketOrderDetailRespDTO>()) // 显式指定列表类型
                .build();
        List<TicketOrderDetailRespDTO> records = new ArrayList<>();
        for (String orderSn : orderSnList)
        {
            TicketOrderDetailRespDTO ticketOrder = ordersService.queryTicketOrderByOrderSn(orderSn);
            records.add(ticketOrder);
        }
        pageResponse.setRecords(records);

        return pageResponse;
    }

    @Override
    public AdminIndexRespDTO getIndexData() {
        AdminIndexRespDTO adminIndexRespDTO = new AdminIndexRespDTO();

        // 设置实时计数器数据
        AdminIndexRespDTO.RealTimeData realTimeData = new AdminIndexRespDTO.RealTimeData();
        //模拟数据
        realTimeData.setTodayViewCount(1200);
        realTimeData.setTodayNewOrderCount(200);
        realTimeData.setTodayTrainCount(50);
        realTimeData.setTodayPassengerCount(800);
        adminIndexRespDTO.setRealTimeData(realTimeData);

        // 设置乘车趋势数据
        AdminIndexRespDTO.TrendData passengerTrend = new AdminIndexRespDTO.TrendData();
        passengerTrend.setDates(new String[]{"2025-04-21", "2025-04-22", "2025-04-23"});
        passengerTrend.setCounts(new Integer[]{1200, 2400, 1800});
        adminIndexRespDTO.setPassengerTrend(passengerTrend);

        // 设置车站流量排行前十名
        AdminIndexRespDTO.RankData stationRank = new AdminIndexRespDTO.RankData();
        stationRank.setNames(new String[]{"北京南站", "上海虹桥", "北京西站", "上海站", "济南站", "车站F", "车站G", "车站H", "车站I", "车站J"});
        stationRank.setValues(new Integer[]{1000, 900, 800, 700, 600, 500, 400, 300, 200, 100});
        adminIndexRespDTO.setStationRank(stationRank);

        // 设置车次上座率
        AdminIndexRespDTO.RankData trainOccupancyRank = new AdminIndexRespDTO.RankData();
        trainOccupancyRank.setNames(new String[]{"G101", "G102", "D311", "D312", "G502"});
        trainOccupancyRank.setValues(new Integer[]{80, 60, 40, 30, 20});
        adminIndexRespDTO.setTrainOccupancyRank(trainOccupancyRank);

        // 设置订单趋势数据
        AdminIndexRespDTO.TrendData orderTrend = new AdminIndexRespDTO.TrendData();
        orderTrend.setDates(new String[]{"2025-04-21", "2025-04-22", "2025-04-23"});
        orderTrend.setCounts(new Integer[]{50, 80, 60});
        adminIndexRespDTO.setOrderTrend(orderTrend);





        return adminIndexRespDTO;
    }
}
