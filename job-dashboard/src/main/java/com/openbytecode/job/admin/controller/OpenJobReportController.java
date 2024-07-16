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
package com.openbytecode.job.admin.controller;

import com.openbytecode.job.admin.service.OpenJobStatisticService;
import com.openbytecode.job.admin.service.OpenJobReportService;
import com.openbytecode.job.api.dto.resp.OpenJobStatisticRespDTO;
import com.openbytecode.job.api.dto.resp.OpenJobChartRespDTO;
import com.openbytecode.job.api.dto.resp.OpenJobTimeChartRespDTO;
import com.openbytecode.job.common.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lijunping on 2022/4/11
 */
@Validated
@RestController
@RequestMapping("/analysis")
public class OpenJobReportController {

    @Autowired
    private OpenJobReportService openJobReportService;

    @Autowired
    private OpenJobStatisticService openJobStatisticService;

    @GetMapping("/statistic")
    public Result<OpenJobStatisticRespDTO> getStatistic() {
        return Result.succeed(openJobStatisticService.getStatistic());
    }

    @GetMapping("/chart")
    public Result<OpenJobChartRespDTO> getChart(@RequestParam(value = "jobId", required = false) Long jobId,
                                                @RequestParam(value = "period", required = false, defaultValue = "7d") String period) {
        return Result.succeed(openJobReportService.getChart(jobId, period));
    }

    @GetMapping("/jobChart")
    public Result<OpenJobTimeChartRespDTO> getJobChart(@RequestParam(value = "jobId") Long jobId,
                                                       @RequestParam(value = "count", required = false, defaultValue = "30") Integer count) {
        return Result.succeed(openJobReportService.getJobChart(jobId, count));
    }


}
