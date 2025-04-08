package com.qdu.dto.domain;

import lombok.Data;

/**
 * 车次基础信息（数据库查询结果的映射对象）
 */
@Data
public class StationToStationRouteDTO {
    /**
     * 车次编号（如 G231）
     */
    private String trainNumber;

    /**
     * 列车类型（1: 动车组, 2: 普速列车）
     */
    private Integer trainType;

    /**
     * 出发站名称（如“北京南站”）
     */
    private String departureStation;

    /**
     * 到达站名称（如“上海虹桥站”）
     */
    private String arrivalStation;

    /**
     * 出发时间（格式 "HH:mm"）
     */
    private String departureTime;

    /**
     * 到达时间（格式 "HH:mm"）
     */
    private String arrivalTime;

    /**
     * 历时（单位：分钟）
     */
    private Integer duration;

    /**
     * 里程（单位：公里）
     */
    private Integer distance;

    /**
     * 是否始发站（true: 是始发站）
     */
    private Boolean departureFlag;

    /**
     * 是否终点站（true: 是终点站）
     */
    private Boolean arrivalFlag;
}