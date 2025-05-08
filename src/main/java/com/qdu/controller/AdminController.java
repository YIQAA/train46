package com.qdu.controller;


import com.qdu.common.convention.result.Result;
import com.qdu.common.convention.result.Results;
import com.qdu.common.page.PageResponse;
import com.qdu.dto.req.admin.StationCreateRepDTO;
import com.qdu.dto.req.admin.ToggleFreezeUserReqDTO;
import com.qdu.dto.req.order.TicketOrderPageQueryReqDTO;
import com.qdu.dto.resp.admin.AdminIndexRespDTO;
import com.qdu.dto.resp.admin.StationListRespDTO;
import com.qdu.dto.resp.admin.TrainListRespDTO;
import com.qdu.dto.resp.admin.UserListRespDTO;
import com.qdu.dto.resp.order.TicketOrderDetailRespDTO;
import com.qdu.dto.resp.ticketList.TrainStationQueryRespDTO;
import com.qdu.service.impl.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin-service")
public class AdminController {

    @Autowired
    private OrdersServiceImpl orderService;
    @Autowired
    private UsersServiceImpl userService;
    @Autowired
    private AdminServiceImpl adminService;
    @Autowired
    private StationsServiceImpl stationService;
    @Autowired
    private TrainsServiceImpl trainService;




    /**
     * 分页查询车票订单
     */
    @GetMapping("/orderList/page")
    public PageResponse<TicketOrderDetailRespDTO> pageTicketOrder(TicketOrderPageQueryReqDTO requestParam) {
        System.out.println("**************************************分页查询车票订单*********************************");
        System.out.println(requestParam);
        return adminService.adminTicketOrderList(requestParam);
    }

    @GetMapping("/stationList")
    public List<StationListRespDTO> getStationList() {
        System.out.println("**************************************查询所有站点*********************************");
        return stationService.getAdminAllStations();
    }
    @PostMapping("/createStation")
    public Boolean createStation(@RequestBody StationCreateRepDTO requestParam) {
        System.out.println("**************************************创建站点*********************************");
        System.out.println(requestParam);
        return stationService.createStation(requestParam);
    }
    @GetMapping("/trainList/query")
    public List<TrainListRespDTO> listTrainStationQuery() {
        System.out.println("-----------------根据trainid查询该车次的所有经过站-------------------------");
        return trainService.listTrainStationQuery();
    }
    @GetMapping("/indexData/query")
    public AdminIndexRespDTO getIndexData() {
        System.out.println("-----------------查询首页数据-------------------------");
        return adminService.getIndexData();
    }
    @GetMapping("/userList/query")
    public List<UserListRespDTO> getUserList() {
        System.out.println("-----------------查询所有用户-------------------------");
        return userService.getUserList();
    }

    //冻结/解冻用户
    @PostMapping("/userFreeze")
    public Result<Void> toggleFreezeUser(@RequestBody ToggleFreezeUserReqDTO requestParam) {
        userService.toggleFreezeUser(requestParam);
        return Results.success();
    }

}
