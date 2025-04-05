package com.qdu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 物理座位表
 * </p>
 *
 * @author 徐增润
 * @since 2025-03-23
 */
@Getter
@Setter
public class Seat implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 物理座位ID
     */
    @TableId(value = "seat_id", type = IdType.AUTO)
    private Integer seatId;

    /**
     * 关联车次ID
     */
    private Integer trainId;

    /**
     * 座位类型
     */
    private Integer seatTypeId;

    /**
     * 车厢号
     */
    private Integer carriageNumber;

    /**
     * 座位号
     */
    private String seatNumber;


    /**
     * 座位状态
     */
    private String status;

}
