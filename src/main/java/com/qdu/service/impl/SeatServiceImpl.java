package com.qdu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qdu.dto.domain.TrainSeatsDTO;
import com.qdu.entity.Seat;
import com.qdu.mapper.SeatMapper;
import com.qdu.service.ISeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl extends ServiceImpl<SeatMapper, Seat> implements ISeatService{

    private final SeatMapper seatMapper;

    @Override
    public List<TrainSeatsDTO> selectAvailableSeats(String trainId, Integer mileage) {
        return seatMapper.selectAvailableSeats(trainId, mileage);
    }
}
