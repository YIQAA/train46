package com.qdu.dto.resp.admin;

import lombok.*;

@Data
@Getter
@Setter
public class AdminIndexRespDTO {


    // 实时计数器数据
    private RealTimeData realTimeData;
    // 乘车趋势数据
    private TrendData passengerTrend;
    // 车站流量排行
    private RankData stationRank;
    // 车次上座率
    private RankData trainOccupancyRank;
    // 订单趋势数据
    private TrendData orderTrend;


    // 实时数据模型
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RealTimeData {
        private Integer todayViewCount;       // 今日浏览人数
        private Integer todayNewOrderCount;   // 今日新增订单
        private Integer todayTrainCount;      // 今日运行车次
        private Integer todayPassengerCount;  // 今日运载乘客
    }

    // 趋势数据模型（乘车人数/订单共用结构）

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TrendData {
        private String[] dates;               // 日期序列 ["2023-01-01",...]
        private Integer[] counts;             // 数值序列 [1200,...]
    }

    // 排行数据模型（车站/车次共用结构）
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RankData {
        private String[] names;               // 名称序列 ["车站A","车次B",...]
        private Integer[] values;              // 排行数值 [1000, 800,...]
    }

}
