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
package com.openbytecode.job.admin.service;

import com.openbytecode.job.api.dto.resp.OpenJobChartRespDTO;
import com.openbytecode.job.api.dto.resp.OpenJobTimeChartRespDTO;

import java.time.LocalDateTime;

/**
 * @author lijunping on 2022/4/11
 */
public interface OpenJobReportService {

    /**
     * 生成报表
     * @param now
     */
    void generateReport(LocalDateTime now);

    /**
     * 获取任务执行次数折线图
     * @param jobId
     * @param period 周期
     * @return
     */
    OpenJobChartRespDTO getChart(Long jobId, String period);

    /**
     * 获取任务执行耗时统计图
     * @param jobId
     * @param count 条数
     * @return
     */
    OpenJobTimeChartRespDTO getJobChart(Long jobId, Integer count);
}
