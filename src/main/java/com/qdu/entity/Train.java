package com.qdu.entity;

import java.io.Serializable;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 车次基础信息表
 * </p>
 *
 * @author 徐增润
 * @since 2025-03-23
 */
@Getter
@Setter
public class Train implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 车次编号(如G231)
     */
    private String trainNumber;

    /**
     * 列车类型
     */
    private String trainType;

    /**
     * 始发站ID
     */
    private Integer startStationId;

    /**
     * 终点站ID
     */
    private Integer endStationId;

    /**
     * 出发时间
     */
    private LocalTime departureTime;

    /**
     * 总行程时间（分钟）
     */
    private Integer totalTravelTime;
    /**
     * 总距离（公里）
     */
    private Integer totalDistance;
}
