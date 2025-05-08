package com.qdu.service.impl;

import com.qdu.common.page.PageResponse;
import com.qdu.dto.req.order.TicketOrderPageQueryReqDTO;
import com.qdu.dto.resp.admin.*;
import com.qdu.dto.resp.order.TicketOrderDetailRespDTO;
import com.qdu.mapper.*;
import com.qdu.service.IAdminService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements IAdminService {

    private final OrdersMapper ordersMapper;
    private final OrdersServiceImpl ordersService;
    private final UsersMapper usersMapper;
    private final DailyTrainMapper dailyTrainMapper;
    private final StationMapper  stationMapper;
    private final TrainMapper trainMapper;



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

        //当天数据
        realTimeData.setTodayViewCount(getTodayViewCount());
        realTimeData.setTodayNewOrderCount(getTodayOrderCount());
        realTimeData.setTodayTrainCount(getTodayTrainCount());
        realTimeData.setTodayPassengerCount(getTodayPassengerCount());
        adminIndexRespDTO.setRealTimeData(realTimeData);

        // 设置乘车趋势数据
        AdminIndexRespDTO.TrendData passengerTrend = getPassengerTrend(7); // 最近7天趋势
        adminIndexRespDTO.setPassengerTrend(passengerTrend);


        // 设置车站流量排行前十名
        adminIndexRespDTO.setStationRank(getStationRank());

        // 设置车次上座率
//        adminIndexRespDTO.setTrainOccupancyRank(getTrainOccupancyTop5());

        // 设置订单趋势数据
//        AdminIndexRespDTO.TrendData orderTrend = getOrderTrend(7);
//        adminIndexRespDTO.setOrderTrend(orderTrend);


        return adminIndexRespDTO;
    }
    //今天的浏览人数
    public Integer getTodayViewCount(){
        return usersMapper.getTodayViewCount();
    }
    //今天的订单数
    public Integer getTodayOrderCount(){
        return ordersMapper.getTodayOrderCount();
    }
    //今天的车次数
    public Integer getTodayTrainCount(){
        return dailyTrainMapper.getTodayTrainCount();
    }
    //今天的乘客数
    public Integer getTodayPassengerCount(){
        return ordersMapper.getTodayPassengerCount();
    }
    //每日乘车趋势
    private AdminIndexRespDTO.TrendData getPassengerTrend(int days) {
        List<PassengerTrendDTO> trendList = ordersMapper.getPassengerTrend(days);

        // 处理日期排序和格式
        Map<String, Integer> trendMap = trendList.stream()
                .collect(Collectors.toMap(
                        PassengerTrendDTO::getDate,
                        PassengerTrendDTO::getCount
                ));

        // 生成完整日期序列
        List<String> dateList = generateDateSequence(days);

        // 填充数据
        AdminIndexRespDTO.TrendData trendData = new AdminIndexRespDTO.TrendData();
        trendData.setDates(dateList.toArray(new String[0]));
        trendData.setCounts(dateList.stream()
                .map(date -> trendMap.getOrDefault(date, 0))
                .toArray(Integer[]::new));

        return trendData;
    }


    //订单趋势
    private AdminIndexRespDTO.TrendData getOrderTrend(int days) {
        // 获取数据库原始数据
        List<OrderTrendDTO> dbData = ordersMapper.getOrderTrend(days);

        // 转换为Map格式
        Map<String, Integer> dataMap = dbData.stream()
                .collect(Collectors.toMap(
                        OrderTrendDTO::getDate,
                        OrderTrendDTO::getCount
                ));

        // 生成完整日期序列
        List<String> dateList = generateDateSequence(days);

        // 构建趋势数据对象
        AdminIndexRespDTO.TrendData trendData = new AdminIndexRespDTO.TrendData();
        trendData.setDates(dateList.toArray(new String[0]));
        trendData.setCounts(
                dateList.stream()
                        .map(date -> dataMap.getOrDefault(date, 0))
                        .toArray(Integer[]::new)
        );

        return trendData;
    }

    //车站流量排行
    private AdminIndexRespDTO.RankData getStationRank() {
        List<StationFlowDTO> flowList = stationMapper.getStationTop5Flow();

        AdminIndexRespDTO.RankData rankData = new AdminIndexRespDTO.RankData();
        rankData.setNames(flowList.stream()
                .map(StationFlowDTO::getStationName)
                .toArray(String[]::new));
        rankData.setValues(flowList.stream()
                .map(StationFlowDTO::getFlowCount)
                .toArray(Integer[]::new));

        return rankData;
    }



    //获取车次上座率
    public AdminIndexRespDTO.RankData getTrainOccupancyTop5() {
        List<Map<String, Object>> list = trainMapper.getSimpleTrainOccupancy();

        return new AdminIndexRespDTO.RankData(
                list.stream()
                        .map(m -> m.getOrDefault("train_number", "未知车次").toString())
                        .toArray(String[]::new),
                list.stream()
                        .map(m -> ((Number) m.getOrDefault("occupancy_rate", 0)).intValue())
                        .toArray(Integer[]::new)
        );
    }







    // 生成连续日期工具方法
    private List<String> generateDateSequence(int days) {
        List<String> dateList = new ArrayList<>();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (int i = days - 1; i >= 0; i--) {
            dateList.add(today.minusDays(i).format(formatter));
        }
        return dateList;
    }

}
