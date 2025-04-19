package com.qdu.service;

import com.qdu.dto.req.user.InsuranceCreateReqDTO;
import com.qdu.dto.resp.user.InsuranceListRespDTO;
import com.qdu.entity.InsuranceOrders;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 保险订单表 服务类
 * </p>
 *
 * @author 徐增润
 * @since 2025-04-18
 */
public interface IInsuranceOrdersService extends IService<InsuranceOrders> {

    //创建保险
    public String createInsuranceOrder(InsuranceCreateReqDTO insuranceCreateReqDTO);

    //查询保险列表
    public List<InsuranceListRespDTO> queryInsuranceOrder(Integer userId);

}
