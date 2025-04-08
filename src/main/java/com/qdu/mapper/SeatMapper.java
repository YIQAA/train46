package com.qdu.mapper;

import com.qdu.dto.domain.TrainSeatsDTO;
import com.qdu.entity.Seat;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 物理座位表 Mapper 接口
 * </p>
 *
 * @author 徐增润
 * @since 2025-03-23
 */
public interface SeatMapper extends BaseMapper<Seat> {

    List<TrainSeatsDTO> selectAvailableSeats(@Param("trainId") String trainId,
                                             @Param("mileage") Integer mileage);

    // 查询符合条件的可用座位ID（新增trainId参数）
    List<Integer> selectAvailableSeatIds(
            @Param("trainId") int trainId,      // 新增参数
            @Param("seatType") int seatType,
            @Param("letters") List<String> letters,
            @Param("seatCount") int seatCount
    );

    // 锁定方法新增trainId参数
    int lockSeatsByIds(
            @Param("trainId") int trainId,     // 新增参数
            @Param("seatIds") List<Integer> seatIds
    );
    // 查询可用座位（按车厢→排号→座位字母排序）
    List<Integer> selectRandomAvailableSeats(
            @Param("trainId") int trainId,
            @Param("seatType") int seatType,
            @Param("count") int count
    );
    //根据座位类型查询座位基础价格
    Double selectSeatPriceByType(@Param("seatType") int seatType);


}
