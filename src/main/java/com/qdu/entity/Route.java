package com.qdu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 路线时刻表
 * </p>
 *
 * @author 徐增润
 * @since 2025-03-23
 */
@Getter
@Setter
public class Route implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 路线ID
     */
    @TableId(value = "route_id", type = IdType.AUTO)
    private Integer routeId;

    /**
     * 车次编号
     */
    private String trainNumber;

    /**
     * 车站顺序
     */
    private Integer stationSequence;

    /**
     * 经停车站ID
     */
    private Integer stationId;

    /**
     * 距始发站里程(km)
     */
    private Integer mileage;

    /**
     * 停留时间(分钟)
     */
    private Integer stopDuration;

    /**
     * 到达本站距离始发站的时间偏移（单位分钟）
     */
    private Integer arrivalTimeOffset;
}
