/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qdu.dto.req.order;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 车票订单创建请求参数
 *
 */
@Data
public class TicketOrderCreateReqDTO {

    /**
     * 用户 ID
     */
    private Integer userId;

    /**
     * 车次 ID
     */
    private Integer trainId;

    /**
     * 出发站点
     */
    private String departureStation;

    /**
     * 到达站点
     */
    private String arrivalStation;

    /**
     * 乘客明细
     */
    private List<TicketOrderItemCreateReqDTO> passengers;

    /**
     * 是否自选座位
     */
    private Boolean isChooseSeat;

    /**
     * 选择的座位
     */
    private List<String> chooseSeats;
}
