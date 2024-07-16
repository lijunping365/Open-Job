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
package com.openbytecode.job.admin.task;

import com.openbytecode.job.admin.mapper.OpenJobLogMapper;
import com.openbytecode.job.admin.service.OpenJobReportService;
import com.openbytecode.job.common.time.LocalDateTimeUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author lijunping on 2022/4/22
 */
@Component
public class SystemTask {

    @Value("${com.openbytecode.log.interval:7}")
    private Integer interval;

    private final OpenJobLogMapper openJobLogMapper;
    private final OpenJobReportService openJobReportService;

    public SystemTask(OpenJobLogMapper openJobLogMapper,
                      OpenJobReportService openJobReportService) {
        this.openJobLogMapper = openJobLogMapper;
        this.openJobReportService = openJobReportService;
    }


    /**
     * 每天 1：00：00 执行
     *
     * 1. 执行生成当日报表任务
     * 2. 执行清除n天前的任务日志
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void addReportTask(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = now.plusDays(-1);
        LocalDateTime time = LocalDateTimeUtil.getDayEnd(yesterday);
        openJobReportService.generateReport(yesterday);
        openJobLogMapper.clearLog(time, interval);
    }
}
