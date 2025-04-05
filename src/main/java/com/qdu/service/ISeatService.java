package com.qdu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qdu.dto.domain.TrainSeatsDTO;
import com.qdu.entity.Seat;

import java.util.List;

public interface ISeatService extends IService<Seat> {

    //查询某车次中某类型的座位信息
    List<TrainSeatsDTO> selectAvailableSeats(String trainId, Integer startSeq, Integer endSeq, Integer mileage);
}
