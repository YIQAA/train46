package com.qdu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.qdu.common.convention.exception.ServiceException;
import com.qdu.common.page.PageResponse;
import com.qdu.dto.domain.StationToStationRouteDTO;
import com.qdu.dto.req.RefundTicketReqDTO;
import com.qdu.dto.req.order.TicketOrderCreateReqDTO;
import com.qdu.dto.req.order.TicketOrderItemCreateReqDTO;
import com.qdu.dto.req.order.TicketOrderPageQueryReqDTO;
import com.qdu.dto.resp.user.RefundTicketRespDTO;
import com.qdu.dto.resp.order.TicketOrderDetailRespDTO;
import com.qdu.dto.resp.order.TicketOrderPassengerDetailRespDTO;
import com.qdu.entity.OrderPassengers;
import com.qdu.entity.Orders;
import com.qdu.entity.Seat;
import com.qdu.entity.Station;
import com.qdu.mapper.*;
import com.qdu.service.IOrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单主表 服务实现类
 * </p>
 *
 * @author 徐增润
 * @since 2025-03-28
 */
@Service
@RequiredArgsConstructor
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements IOrdersService {

    private final TrainsServiceImpl trainsService ;
    private final StationsServiceImpl stationsService ;
    private final UsersServiceImpl usersService ;
    private final OrdersMapper ordersMapper;
    private final SeatMapper seatMapper;
    private final StationMapper stationMapper;
    private final OrderPassengersMapper orderPassengersMapper;

    private final RedisTemplate<String, Object> redisTemplate;
    private final UsersMapper usersMapper;


    // 创建订单
    @Override
    public String createTicketOrder(TicketOrderCreateReqDTO requestParam) {

        // 获取车次信息
        Integer startStationId = stationsService.getStationIdByName(requestParam.getDepartureStation());
        Integer endStationId = stationsService.getStationIdByName(requestParam.getArrivalStation());
        StationToStationRouteDTO stationToStationRouteDTO = trainsService.findTrainInfoByTrainId(requestParam.getTrainId(), startStationId, endStationId);
        LocalDate departureDate = trainsService.findDepartureDateByTrainId(requestParam.getTrainId());

        String orderSn = generateOrderId(String.valueOf(requestParam.getUserId()), stationToStationRouteDTO.getTrainNumber(),departureDate);

        Orders orders = new Orders();
        orders.setOrderNumber(orderSn);
        orders.setUserId(requestParam.getUserId());
        orders.setTrainId(requestParam.getTrainId());
        orders.setTrainNumber(stationToStationRouteDTO.getTrainNumber());
        orders.setTrainDate(departureDate);
        orders.setStartStationId(startStationId);
        orders.setEndStationId(endStationId);
        orders.setDepartureTime(LocalTime.parse(stationToStationRouteDTO.getDepartureTime()));
        orders.setArrivalTime(LocalTime.parse(stationToStationRouteDTO.getArrivalTime()));
        orders.setPaymentAmount(0);
        orders.setStatus(0);
        // 插入订单主表
        ordersMapper.insert(orders);
        //根据订单号获得订单id
        Integer orderId = ordersMapper.findOrderIdByOrderSn(orderSn);
        System.out.println("订单id："+orderId);

        System.out.println("=====================火车信息===========================");
        System.out.println(stationToStationRouteDTO);

        // 处理乘客信息
        List<TicketOrderItemCreateReqDTO> passengers = requestParam.getPassengers();
        Integer passengerAmount = passengers.size();
        Integer seatType = passengers.get(0).getSeatType();

        //座位详细
        List<Integer> seatIds = new ArrayList<>();
        //如果支持选座
        if(requestParam.getIsChooseSeat())
        {
            //选座数量、选座类型
            Integer chooseSeatAmount =requestParam.getChooseSeats().size();
            List<String> chooseSeats = requestParam.getChooseSeats();
            // 处理座位选择，分割
            Map<Integer, List<String>> groups = parseGroups(chooseSeats);
            for(Integer key : groups.keySet())
            {
                List<Integer> seatList = lockSeatsByPosition(requestParam.getTrainId(),seatType,groups.get(key));
                System.out.println("=====================座位1===========================");
                System.out.println(groups.get(key));
                System.out.println(seatList);
                seatIds.addAll(seatList) ;
            }
            //如果乘客数大于选座数，在随机几个座位
            if(passengerAmount>chooseSeatAmount)
            {
                List<Integer> seatList = lockRandomSeats(requestParam.getTrainId(),seatType,passengerAmount-chooseSeatAmount);
                System.out.println("=====================座位2===========================");
                System.out.println(seatList);
                seatIds.addAll(seatList);
            }
        }
        //如果不支持选座，全部随机安排
        else {
            List<Integer> seatList = lockRandomSeats(requestParam.getTrainId(),seatType,passengerAmount);
            System.out.println("=====================座位3===========================");
            System.out.println(seatList);
            seatIds.addAll(seatList);
        }

        System.out.println("=====================座位分布===========================");
        System.out.println("乘客数"+passengerAmount+"选的座位数"+requestParam.getChooseSeats().size());
        System.out.println("列车id"+requestParam.getTrainId()+"+座位类型："+seatType+"座位号："+seatIds);


        Integer oderAmount = 0;
        // 插入订单乘客表
        for (int i = 0; i < passengers.size(); i++) {
            OrderPassengers orderPassengers = new OrderPassengers();
            orderPassengers.setOrderId(orderId);
            orderPassengers.setSeatId(seatIds.get(i));
            orderPassengers.setPassengerName(passengers.get(i).getRealName());
            orderPassengers.setPassengerIdType(passengers.get(i).getIdType());
            orderPassengers.setPassengerIdCard(passengers.get(i).getIdCard());
            orderPassengers.setTicketType(passengers.get(i).getTicketType());
            Double amount = 0.0;
            amount = seatMapper.selectSeatPriceByType(passengers.get(i).getSeatType()) * stationToStationRouteDTO.getDistance();
            if (passengers.get(i).getTicketType() == 1) amount = amount * 0.8;
            orderPassengers.setAmount(amount.intValue());
            oderAmount += orderPassengers.getAmount();
            // 插入订单乘客表
            orderPassengersMapper.insert(orderPassengers);
        }

        ordersMapper.update(new Orders(),
                new UpdateWrapper<Orders>()
                        .set("payment_amount", oderAmount)
                        .eq("order_id", orderId));


        //删除redis
        if (true) {
            // 获取订单的出发城市、到达城市和出发日期
            // 这里假设可以从订单信息中获取这些数据
            Station startStation = stationMapper.selectOne(new QueryWrapper<Station>().eq("station_name", requestParam.getDepartureStation()));
            Station endStation = stationMapper.selectOne(new QueryWrapper<Station>().eq("station_name", requestParam.getArrivalStation()));
            String fromCity = startStation.getCityCode();
            String toCity = endStation.getCityCode();

            String queryKey = "ticket:query:" + fromCity + ":" + toCity + ":" + departureDate;
            String pageQueryKey = "ticket:pageQuery:" + fromCity + ":" + toCity + ":" + departureDate;
            redisTemplate.delete(queryKey);
            redisTemplate.delete(pageQueryKey);
        }
        return orderSn;
    }


    /**
    * 取消订单（支持多座位解锁）
    * @param orderSn 订单号
    * @return true-取消成功, false-订单不存在或状态不可取消
    */
    @Transactional(rollbackFor = Exception.class)
    public Boolean cancelOrder(String orderSn) {
        // 1. 订单状态更新校验
        int orderUpdateCount = ordersMapper.updateOrderStatusToCanceled(orderSn);
        if (orderUpdateCount == 0) {
            return false;
        }
        // 2. 执行座位解锁
        int seatUnlockCount = ordersMapper.unlockSeatsByOrderSn(orderSn);
        return true;
    }

    @Override
    public Integer getOrderStatusByOrderSn(String orderSn) {
        return ordersMapper.getOrderStatusByOrderSn(orderSn);
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean pay(String orderSn) {

        // 1. 更新订单状态（0→1）
        int orderUpdate = ordersMapper.updateOrderStatusToPaid(orderSn);
        if (orderUpdate == 0) {
            return false;
        }

        // 2. 批量占用座位（locked→occupied）
        int seatUpdate = ordersMapper.occupySeatsByOrderSn(orderSn);
        if (seatUpdate == 0) {
            return false;
        }
        return true;
    }

    /**
     * 退票
     * @param requestParam
     * @return
     */
    @Transactional
    public RefundTicketRespDTO commonTicketRefund(RefundTicketReqDTO requestParam) {
        Orders orders = ordersMapper.selectOne(new QueryWrapper<Orders>().eq("order_number", requestParam.getOrderSn()));

        String orderSn = requestParam.getOrderSn();
        if (orders == null)throw new ServiceException("订单不存在");

        if (orders.getStatus() != 1)throw new ServiceException("当前状态不允许退款");
        // 退票人员
        List<String> RefundPassengerList = requestParam.getSubOrderRecordIdReqList();
        if (RefundPassengerList.size() == 0)throw new ServiceException("退票人员不能为空");

        List<Integer> RefundPassengerIdList = new ArrayList<>();
        for (String passenger : RefundPassengerList)
        {
            Integer passengerId = Integer.valueOf(passenger);
            RefundPassengerIdList.add(passengerId);
        }
        //订单人员
        List<OrderPassengers> orderPassengers = orderPassengersMapper.selectList(
                new QueryWrapper<OrderPassengers>().eq("order_id", orders.getOrderId()));

        Boolean isSuccess = false;

        //全部退款 1
        if (RefundPassengerIdList.size() == orderPassengers.size()){
            // 更新订单状态（1→4）
            ordersMapper.update(new Orders(),new UpdateWrapper<Orders>().set("status", 4).eq("order_id", orders.getOrderId()));
            for (OrderPassengers orderPassenger : orderPassengers){
                seatMapper.update(new Seat(),new UpdateWrapper<Seat>().set("status", "available")
                                                                        .eq("seat_id", orderPassenger.getSeatId())
                                                                        .eq("status", "occupied")   );
            }
            isSuccess = true;
        }
        //部分退款 0
        else{
            for (OrderPassengers orderPassenger : orderPassengers)
                for (Integer passengerId : RefundPassengerIdList)
                {
                    if (orderPassenger.getPassengerId().equals(passengerId))
                    {
                        orderPassengersMapper.deleteById(orderPassenger.getPassengerId());
                        seatMapper.update(new Seat(),new UpdateWrapper<Seat>().set("status", "available")
                                                                                .eq("seat_id", orderPassenger.getSeatId())
                                                                                .eq("status", "occupied"));
                        ordersMapper.update(new Orders(),
                                new UpdateWrapper<Orders>()
                                        .set("payment_amount", orders.getPaymentAmount() - orderPassenger.getAmount())
                                        .eq("order_id", orders.getOrderId()));
                    }
                }
            isSuccess = true;
        }
        // 删除Redis缓存
        if (isSuccess) {
            String fromCity = "fromCityCode";
            String toCity = "toCityCode";
            LocalDate departureDate = LocalDate.now();
            String queryKey = "ticket:query:" + fromCity + ":" + toCity + ":" + departureDate;
            String pageQueryKey = "ticket:pageQuery:" + fromCity + ":" + toCity + ":" + departureDate;
            redisTemplate.delete(queryKey);
            redisTemplate.delete(pageQueryKey);
        }

        return null;
    }

    //根据orderSn查询订单
    public TicketOrderDetailRespDTO queryTicketOrderByOrderSn(String orderSn) {
        //根据订单号查询  订单实体
        Orders orders = ordersMapper.selectOne(new QueryWrapper<Orders>().eq("order_number", orderSn));
        List<OrderPassengers> orderPassengers = orderPassengersMapper.selectList(new QueryWrapper<OrderPassengers>().eq("order_id", orders.getOrderId()));

        //封装返回数据
        TicketOrderDetailRespDTO ticketOrderDetailRespDTO = new TicketOrderDetailRespDTO();

        ticketOrderDetailRespDTO.setOrderSn(orders.getOrderNumber());
        ticketOrderDetailRespDTO.setUserName(usersMapper.getUserNameByUserId(orders.getUserId()));
        ticketOrderDetailRespDTO.setTrainId(orders.getTrainId());
        ticketOrderDetailRespDTO.setDepartureStation(stationsService.getStationNameById(orders.getStartStationId()));
        ticketOrderDetailRespDTO.setArrivalStation(stationsService.getStationNameById(orders.getEndStationId()));
        ticketOrderDetailRespDTO.setRidingDate(orders.getTrainDate());
        ticketOrderDetailRespDTO.setTrainNumber(orders.getTrainNumber());
        ticketOrderDetailRespDTO.setDepartureTime(orders.getDepartureTime());
        ticketOrderDetailRespDTO.setArrivalTime(orders.getArrivalTime());
        ticketOrderDetailRespDTO.setOrderTime(orders.getCreatedAt());
        ticketOrderDetailRespDTO.setOrderStatus(orders.getStatus());

        List<TicketOrderPassengerDetailRespDTO> passengerDetails = new ArrayList<>();

        for (OrderPassengers orderPassenger : orderPassengers)
        {
            TicketOrderPassengerDetailRespDTO OrderPassengerDetail = new TicketOrderPassengerDetailRespDTO();
            OrderPassengerDetail.setId(orderPassenger.getPassengerId());
            OrderPassengerDetail.setUserId(orders.getUserId());
            OrderPassengerDetail.setUsername(usersService.getUserNameByUserId(orders.getUserId()));

            Seat seat = seatMapper.selectOne(new QueryWrapper<Seat>().eq("seat_id", orderPassenger.getSeatId()));
            OrderPassengerDetail.setSeatType(seat.getSeatTypeId());
            OrderPassengerDetail.setCarriageNumber(seat.getCarriageNumber());
            OrderPassengerDetail.setSeatNumber(seat.getSeatNumber());
            OrderPassengerDetail.setRealName(orderPassenger.getPassengerName());
            OrderPassengerDetail.setIdType(orderPassenger.getPassengerIdType());
            OrderPassengerDetail.setIdCard(orderPassenger.getPassengerIdCard());
            OrderPassengerDetail.setTicketType(orderPassenger.getTicketType());
            OrderPassengerDetail.setAmount(orderPassenger.getAmount());
            OrderPassengerDetail.setStatus(orders.getStatus());

            passengerDetails.add(OrderPassengerDetail);
        }
        ticketOrderDetailRespDTO.setPassengerDetails(passengerDetails);

        return ticketOrderDetailRespDTO;
    }

    //OrderList ,订单页面
    public PageResponse<TicketOrderDetailRespDTO> pageTicketOrderList(TicketOrderPageQueryReqDTO requestParam)
    {
        //  0：未完成 1：未出行 2：历史订单
        Integer orderStatus = requestParam.getStatusType();

        //根据userid查所有orderSn
        List<String> orderSnList = ordersMapper.selectOrderSnByUserId(requestParam.getUserId());

        PageResponse<TicketOrderDetailRespDTO> pageResponse = PageResponse.<TicketOrderDetailRespDTO>builder()
                .current(requestParam.getCurrent())
                .size(requestParam.getSize())
                .records(new ArrayList<TicketOrderDetailRespDTO>()) // 显式指定列表类型
                .build();
        List<TicketOrderDetailRespDTO> records = new ArrayList<>();
        for (String orderSn : orderSnList)
        {
            TicketOrderDetailRespDTO ticketOrder = queryTicketOrderByOrderSn(orderSn);

            //如果订单状态符合，则加入列表

            if (orderStatus == 2 && ticketOrder.getOrderStatus() != 0 && ticketOrder.getOrderStatus() != 1) records.add(ticketOrder);
            else if (orderStatus == ticketOrder.getOrderStatus()) records.add(ticketOrder);
        }
        pageResponse.setRecords(records);

        return pageResponse;
    }



    // 用于保证同一毫秒内的序列号
    private static final AtomicInteger sequence = new AtomicInteger(0);
    //生成订单号
    public String generateOrderId(String userId, String trainNumber, LocalDate departureDate) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String datePart = formatter.format(departureDate); // 6位日期

        // 处理车次编号：提取数字并取后3位，不足补零
        String trainDigits = trainNumber.replaceAll("\\D+", "");
        trainDigits = String.format("%3s", trainDigits).replace(' ', '0');
        trainDigits = trainDigits.substring(trainDigits.length() - 3);

        // 处理用户ID：提取数字并取后3位，不足补零
        String userDigits = userId.replaceAll("\\D+", "");
        userDigits = String.format("%3s", userDigits).replace(' ', '0');
        userDigits = userDigits.substring(userDigits.length() - 3);

        // 时间戳及序列号：4位（2位序列 + 2位时间戳）
        long timestamp = System.currentTimeMillis();
        int seq = sequence.getAndIncrement() % 100; // 两位序列号
        String timePart = String.format("%02d", seq) + String.format("%02d", (timestamp / 10) % 100);

        // 组合所有部分
        return datePart + trainDigits + userDigits + timePart;
    }


    // 解析组号（如"A0","C0"→组0需A+C连座）
    private Map<Integer, List<String>> parseGroups(List<String> chooseSeats) {
        if (chooseSeats == null || chooseSeats.isEmpty()) {
            return Collections.emptyMap();
        }

        return chooseSeats.stream()
                .filter(s -> s.length() >= 2) // 过滤无效格式（如空字符串或单字符）
                .filter(s -> Character.isLetter(s.charAt(0))) // 首字符必须是字母
                .filter(s -> isNumeric(s.substring(1)))       // 组号必须是数字
                .collect(Collectors.groupingBy(
                        s -> Integer.parseInt(s.substring(1)),
                        Collectors.mapping(
                                s -> s.substring(0, 1).toUpperCase(), // 统一转大写
                                Collectors.toList()
                        )
                ));
    }

    // 辅助方法：验证字符串是否为数字
    private boolean isNumeric(String str) {
        return str.matches("\\d+");
    }

    // 根据ABCDF选座
    @Transactional
    public List<Integer> lockSeatsByPosition(int trainId, int seatType, List<String> letters) {
        final int seatCount = letters.size();
        System.out.println("=====================调用lockSeatsProcess===========================");

        // STEP1: 查询可用座位
        List<Integer> seatIds = seatMapper.selectAvailableSeatIds(
                trainId,     // 传入车次ID
                seatType,
                letters,
                seatCount
        );

        if (CollectionUtils.isEmpty(seatIds)) {
            return Collections.emptyList();
        }
        System.out.println("查到座位！！座位id："+seatIds);
        // STEP2: 尝试锁定
        int affected = seatMapper.lockSeatsByIds(
                trainId,    // 传入车次ID用于二次校验
                seatIds
        );

        if (affected == seatCount) {
            return seatIds;
        }

        else {
            throw new IllegalStateException("座位锁定失败，请重新选择");
        }
    }

    //随机选座
    @Transactional
    public List<Integer> lockRandomSeats(int trainId, int seatType, int count) {
        // 1. 查询可用座位
        List<Integer> seatIds = seatMapper.selectRandomAvailableSeats(
                trainId,
                seatType,
                count
        );

        if (seatIds.size() < count) {
            throw new IllegalStateException("余票不足");
        }

        // 2. 尝试锁定
        int affected = seatMapper.lockSeatsByIds(trainId, seatIds);

        // 3. 校验锁定结果
        if (affected == count) {
            return seatIds;
        } else {
            throw new IllegalStateException("选座失败，请重试"); // 触发事务回滚
        }
    }
}
