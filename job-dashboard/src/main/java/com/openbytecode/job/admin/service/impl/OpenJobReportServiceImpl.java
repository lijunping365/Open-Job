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
package com.openbytecode.job.admin.service.impl;

import com.openbytecode.job.admin.entity.OpenJobDO;
import com.openbytecode.job.admin.entity.OpenJobLogDO;
import com.openbytecode.job.admin.entity.OpenJobReportDO;
import com.openbytecode.job.admin.mapper.OpenJobLogMapper;
import com.openbytecode.job.admin.mapper.OpenJobMapper;
import com.openbytecode.job.admin.mapper.OpenJobReportMapper;
import com.openbytecode.job.admin.service.OpenJobReportService;
import com.openbytecode.job.common.comparator.MapComparable;
import com.openbytecode.job.common.enums.JobTimeChartEnum;
import com.openbytecode.job.common.time.LocalDateTimeUtil;
import com.openbytecode.job.api.dto.resp.OpenJobChartRespDTO;
import com.openbytecode.job.api.dto.resp.OpenJobTimeChartRespDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lijunping on 2022/4/11
 */
@Slf4j
@Service
public class OpenJobReportServiceImpl implements OpenJobReportService {

    private final OpenJobMapper openJobMapper;
    private final OpenJobLogMapper openJobLogMapper;
    private final OpenJobReportMapper openJobReportMapper;

    public OpenJobReportServiceImpl(OpenJobMapper openJobMapper,
                                    OpenJobLogMapper openJobLogMapper,
                                    OpenJobReportMapper openJobReportMapper) {
        this.openJobMapper = openJobMapper;
        this.openJobLogMapper = openJobLogMapper;
        this.openJobReportMapper = openJobReportMapper;
    }

    @Override
    public void generateReport(LocalDateTime now) {
        LocalDateTime startTime = LocalDateTimeUtil.getDayStart(now);
        LocalDateTime endTime = LocalDateTimeUtil.getDayEnd(now);

        List<OpenJobDO> jobDOS = openJobMapper.selectAll();
        if (CollectionUtils.isEmpty(jobDOS)){
            return;
        }

        for (OpenJobDO jobDO : jobDOS) {
            int scheduleTotalCount = openJobLogMapper.getScheduleTotalCount(jobDO.getId(), startTime, endTime);
            int scheduleSucceedCount = openJobLogMapper.getScheduleSuccessTotalCount(jobDO.getId(), startTime, endTime);
            OpenJobReportDO openJobReportDO = new OpenJobReportDO();
            openJobReportDO.setJobId(jobDO.getId());
            openJobReportDO.setTaskExecTotalCount(scheduleTotalCount);
            openJobReportDO.setTaskExecSuccessCount(scheduleSucceedCount);
            openJobReportDO.setCreateTime(endTime);
            openJobReportMapper.insert(openJobReportDO);
        }
    }

    @Override
    public OpenJobChartRespDTO getChart(Long jobId, String period) {
        LocalDateTime endTime = LocalDateTime.now().plusDays(-1);
        LocalDateTime startTime = JobTimeChartEnum.getStartTime(endTime, period);
        List<OpenJobReportDO> openJobReportDOS = openJobReportMapper.queryList(jobId, startTime, endTime);
        if (CollectionUtils.isEmpty(openJobReportDOS)){
            return null;
        }

        Map<LocalDateTime, List<OpenJobReportDO>> groupByDateMap = openJobReportDOS.stream().collect(Collectors.groupingBy(
                OpenJobReportDO::getCreateTime
        ));

        groupByDateMap = MapComparable.sortByKey(groupByDateMap, true);

        OpenJobChartRespDTO chartRespDTOS = new OpenJobChartRespDTO();
        List<String> labels = new ArrayList<>();
        List<Integer> totalCountList = new ArrayList<>();
        List<Integer> successCountList = new ArrayList<>();

        groupByDateMap.forEach((k, v) ->{
            String label = LocalDateTimeUtil.format(k, LocalDateTimeUtil.DATETIME_FORMATTER);
            labels.add(label);
            Integer totalCount = v.stream().map(OpenJobReportDO::getTaskExecTotalCount).reduce(Integer::sum).orElse(0);
            totalCountList.add(totalCount);
            Integer successCount = v.stream().map(OpenJobReportDO::getTaskExecSuccessCount).reduce(Integer::sum).orElse(0);
            successCountList.add(successCount);
        });
        chartRespDTOS.setLabels(labels);
        chartRespDTOS.setTotalCount(totalCountList);
        chartRespDTOS.setSuccessCount(successCountList);

        return chartRespDTOS;
    }

    @Override
    public OpenJobTimeChartRespDTO getJobChart(Long jobId, Integer count) {
        List<OpenJobLogDO> jobLogDOS = openJobLogMapper.getListByJobIdAndCount(jobId, count);
        if (CollectionUtils.isEmpty(jobLogDOS)){
            return null;
        }

        jobLogDOS = jobLogDOS.stream().sorted(Comparator.comparingLong(OpenJobLogDO::getId)).collect(Collectors.toList());

        OpenJobTimeChartRespDTO chartRespDTO = new OpenJobTimeChartRespDTO();
        List<String> labels = new ArrayList<>();
        List<String> takeTime = new ArrayList<>();
        List<Integer> status = new ArrayList<>();

        for (OpenJobLogDO jobLogDO : jobLogDOS) {
            LocalDateTime createTime = jobLogDO.getCreateTime();
            String label = LocalDateTimeUtil.format(createTime, LocalDateTimeUtil.DATETIME_FORMATTER);
            labels.add(label);
            BigDecimal value = BigDecimal.valueOf(jobLogDO.getTakeTime()).divide(BigDecimal.valueOf(1000), 3, BigDecimal.ROUND_HALF_UP);
            takeTime.add(value.toPlainString());
            status.add(jobLogDO.getStatus());
        }
        chartRespDTO.setLabels(labels);
        chartRespDTO.setTakeTime(takeTime);
        chartRespDTO.setStatus(status);
        return chartRespDTO;
    }


}
