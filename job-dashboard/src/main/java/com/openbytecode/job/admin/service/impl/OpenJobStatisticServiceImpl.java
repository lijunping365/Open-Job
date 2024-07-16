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

import com.openbytecode.job.admin.entity.OpenJobInstanceDO;
import com.openbytecode.job.admin.mapper.OpenJobAlarmRecordMapper;
import com.openbytecode.job.admin.mapper.OpenJobInstanceMapper;
import com.openbytecode.job.admin.mapper.OpenJobLogMapper;
import com.openbytecode.job.admin.mapper.OpenJobMapper;
import com.openbytecode.job.admin.service.OpenJobStatisticService;
import com.openbytecode.job.api.dto.resp.OpenJobStatisticRespDTO;
import com.openbytecode.job.common.enums.CommonStatusEnum;
import com.openbytecode.job.common.time.LocalDateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author lijunping on 2022/4/11
 */
@Slf4j
@Service
public class OpenJobStatisticServiceImpl implements OpenJobStatisticService {

    private final OpenJobMapper openJobMapper;
    private final OpenJobLogMapper openJobLogMapper;
    private final OpenJobInstanceMapper instanceMapper;
    private final OpenJobAlarmRecordMapper alarmRecordMapper;

    public OpenJobStatisticServiceImpl(OpenJobMapper openJobMapper,
                                       OpenJobLogMapper openJobLogMapper,
                                       OpenJobInstanceMapper instanceMapper,
                                       OpenJobAlarmRecordMapper alarmRecordMapper) {
        this.openJobMapper = openJobMapper;
        this.openJobLogMapper = openJobLogMapper;
        this.instanceMapper = instanceMapper;
        this.alarmRecordMapper = alarmRecordMapper;
    }

    @Override
    public OpenJobStatisticRespDTO getStatistic() {
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = LocalDateTimeUtil.getDayStart(endTime);
        int taskTotalCount = openJobMapper.getTotalCount();
        int taskRunningCount = openJobMapper.getRunningCount();
        List<OpenJobInstanceDO> instanceList = instanceMapper.getAllInstance();
        int instanceTotalCount = instanceList.size();
        long instanceOnlineCount = instanceList.stream().filter(e-> Objects.equals(e.getStatus(), CommonStatusEnum.YES.getValue())).count();
        int taskExecuteTotalNum = openJobLogMapper.getScheduleTotalCount(null, startTime, endTime);
        int taskExecuteSuccessNum = openJobLogMapper.getScheduleSuccessTotalCount(null, startTime, endTime);
        int alarmCount = alarmRecordMapper.queryCount( null, startTime, endTime);
        return OpenJobStatisticRespDTO.builder()
                .alarmNum(alarmCount)
                .taskTotalNum(taskTotalCount)
                .taskStartedNum(taskRunningCount)
                .executorTotalNum(instanceTotalCount)
                .executorOnlineNum((int) instanceOnlineCount)
                .taskExecuteTotalNum(taskExecuteTotalNum)
                .taskExecuteSuccessNum(taskExecuteSuccessNum)
                .build();
    }
}
