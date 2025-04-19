package com.qdu.service.impl;

import com.qdu.dto.req.user.InsuranceCreateReqDTO;
import com.qdu.entity.InsuranceOrders;
import com.qdu.mapper.InsuranceOrdersMapper;
import com.qdu.service.IInsuranceOrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 保险订单表 服务实现类
 * </p>
 *
 * @author 徐增润
 * @since 2025-04-18
 */
@Service
public class InsuranceOrdersServiceImpl extends ServiceImpl<InsuranceOrdersMapper, InsuranceOrders> implements IInsuranceOrdersService {

    @Override
    public String createInsuranceOrder(InsuranceCreateReqDTO insuranceCreateReqDTO) {
        InsuranceOrders insuranceOrders = new InsuranceOrders();
        insuranceOrders.setUserId(insuranceCreateReqDTO.getUserId());

        insuranceOrders.setInsuranceType(insuranceCreateReqDTO.getInsuranceType());
        insuranceOrders.setDays(insuranceCreateReqDTO.getDays());
        insuranceOrders.setAmount(insuranceCreateReqDTO.getAmount());
        insuranceOrders.setInsuredName(insuranceCreateReqDTO.getInsuredName());
        insuranceOrders.setInsuredIdCard(insuranceCreateReqDTO.getInsuredIdCard());
        insuranceOrders.setInsuredPhone(insuranceCreateReqDTO.getInsuredPhone());
        insuranceOrders.setPolicyHolderName(insuranceCreateReqDTO.getPolicyHolderName());
        insuranceOrders.setPolicyHolderIdCard(insuranceCreateReqDTO.getPolicyHolderIdCard());
        insuranceOrders.setPolicyHolderPhone(insuranceCreateReqDTO.getPolicyHolderPhone());
        insuranceOrders.setStartTime(insuranceCreateReqDTO.getStartTime());
        insuranceOrders.setEndTime(insuranceCreateReqDTO.getEndTime());
        insuranceOrders.setStatus("active");
        baseMapper.insert(insuranceOrders);
        return "1";
    }
}
