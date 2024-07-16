/*
 * Copyright © 2022 organization openbytecode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.openbytecode.job.api.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lijunping on 2022/4/11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OpenJobStatisticRespDTO implements Serializable {

    /**
     * 今日报警数量
     */
    private Integer alarmNum;
    /**
     * 任务总数量
     */
    private Integer taskTotalNum;
    /**
     * 启动任务数量
     */
    private Integer taskStartedNum;
    /**
     * 执行器总数量
     */
    private Integer executorTotalNum;
    /**
     * 执行器在线数量
     */
    private Integer executorOnlineNum;
    /**
     * 今日任务执行总次数
     */
    private Integer taskExecuteTotalNum;
    /**
     * 今日任务执行成功总次数
     */
    private Integer taskExecuteSuccessNum;
    /**
     * 任务下次执行时间
     */
    private String taskNextExecuteTime;
    /**
     * 任务最近一次任务执行用时，单位毫秒
     */
    private Long taskTakeTime;


}
