package com.qdu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 车站信息表
 * </p>
 *
 * @author 徐增润
 * @since 2025-03-23
 */
@Getter
@Setter
public class Station implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 车站ID
     */
    @TableId(value = "station_id", type = IdType.AUTO)
    private Integer stationId;

    /**
     * 车站名称
     */
    private String stationName;

    /**
     * 所属城市
     */
    private String city;

    /**
     * 城市编码
     */
    private String cityCode;
}
