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

package com.qdu.controller;

import com.qdu.mapper.StationMapper;
import com.qdu.service.impl.StationsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

/**
 * 列车站点控制层
 *
 */
@RestController
@RequiredArgsConstructor
public class StationController {


    private final StationsServiceImpl StationService;
    private StationMapper stationMapper;

    /**
     * 根据列车 ID 查询站点信息
     */
//    @GetMapping("/api/ticket-service/train-station/query")
//    public List<TrainStationQueryRespDTO> listTrainStationQuery(String trainId) {
//        return trainStationService.listTrainStationQuery(trainId);
//    }


    /**
     * 查询车站站点集合信息
     */

}

