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
package com.openbytecode.job.admin.event;

import com.openbytecode.job.admin.convert.OpenJobAlarmMessageConvert;
import com.openbytecode.job.admin.domain.AlarmMessage;
import com.openbytecode.job.admin.entity.OpenJobLogDO;
import com.openbytecode.job.admin.mapper.OpenJobLogMapper;
import com.openbytecode.job.common.enums.CommonStatusEnum;
import com.openbytecode.job.admin.service.impl.OpenJobAlarmServiceImpl;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author lijunping on 2022/2/28
 */
@Component
public class JobLogEventListener implements ApplicationListener<JobLogEvent> {

    private final OpenJobLogMapper openJobLogMapper;
    private final OpenJobAlarmServiceImpl alarmService;

    public JobLogEventListener(OpenJobLogMapper openJobLogMapper, OpenJobAlarmServiceImpl alarmService) {
        this.openJobLogMapper = openJobLogMapper;
        this.alarmService = alarmService;
    }

    @Override
    public void onApplicationEvent(JobLogEvent event) {
        final OpenJobLogDO jobLogDO = event.getJobLogDO();
        if (Objects.nonNull(jobLogDO.getId())){
            openJobLogMapper.updateById(jobLogDO);
        }else {
            openJobLogMapper.insert(jobLogDO);
        }

        if (!Objects.equals(jobLogDO.getStatus(), CommonStatusEnum.NO.getValue())){
            return;
        }
        AlarmMessage alarmMessage = OpenJobAlarmMessageConvert.INSTANCE.convert(jobLogDO);
        alarmService.sendAlarm(alarmMessage);
    }
}
