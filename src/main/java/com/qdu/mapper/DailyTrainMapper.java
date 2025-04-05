package com.qdu.mapper;

import com.qdu.entity.DailyTrain;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * <p>
 * 每日车次信息表 Mapper 接口
 * </p>
 *
 * @author 徐增润
 * @since 2025-03-23
 */
public interface DailyTrainMapper extends BaseMapper<DailyTrain> {

    DailyTrain getDailyTrainByNumberAndDate(@Param("trainNumber") String trainNumber,@Param("departureDate") Date departureDate);
}
