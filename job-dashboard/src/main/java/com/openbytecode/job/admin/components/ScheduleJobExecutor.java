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
package com.openbytecode.job.admin.components;

import com.openbytecode.job.admin.service.OpenJobClientService;
import com.openbytecode.starter.schedule.executor.ScheduleTaskExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 定时任务远程调用实现
 * @author lijunping on 2021/8/31
 */
@Slf4j
@Component
public class ScheduleJobExecutor implements ScheduleTaskExecutor {

    private final OpenJobClientService openJobClientService;

    public ScheduleJobExecutor(OpenJobClientService openJobClientService) {
        this.openJobClientService = openJobClientService;
    }

    @Override
    public void execute(List<Long> taskList) {
        if (CollectionUtils.isEmpty(taskList)){
            return;
        }
        taskList.forEach(taskId-> {
            try {
                openJobClientService.invoke(taskId);
            }catch (Exception e){
                log.error(e.getMessage(), e);
            }
        });
    }
}
