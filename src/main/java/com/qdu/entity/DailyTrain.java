package com.qdu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 每日车次信息表
 * </p>
 *
 * @author 徐增润
 * @since 2025-03-23
 */
@Getter
@Setter
@TableName("daily_train")
public class DailyTrain implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 车次ID
     */
    @TableId(value = "train_id", type = IdType.AUTO)
    private Integer trainId;

    /**
     * 车次编号(如G231)
     */
    private String trainNumber;

    /**
     * 实际发车日期
     */
    private LocalDate departureDate;

    /**
     * 车次状态
     */
    private String status;

    /**
     * 座位配置(字符串用,分割数字，9种座位车厢数量)
     */
    private String seatConfig;
}
