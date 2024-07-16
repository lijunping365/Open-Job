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

import com.openbytecode.job.admin.entity.OpenJobLogDO;
import com.openbytecode.job.common.domain.MessageBody;
import com.openbytecode.job.common.enums.JobStatusEnum;
import com.openbytecode.job.common.serialize.SerializationUtils;
import com.openbytecode.job.common.time.LocalDateTimeUtil;
import com.openbytecode.rpc.core.Message;
import com.openbytecode.rpc.core.exception.RpcException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class JobLogEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public JobLogEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    /**
     * 保存或更新异常日志，注意第二个参数的 Exception 是不能为空的，因为任务成功日志会在响应拦截器中进行
     * 保存触发时机：当 logId 为 null 时执行保存即新增（触发保存动作只能发生在调用请求拦截器之前，即没有健康节点时）
     * 更新触发时机：当 logId 不为 null 且 rpc 调用异常时会触发日志更新（即经过了请求拦截器，但是 rpc 出现了异常）
     * @param message
     * @param exception
     */
    public void saveOrUpdateLog(Message message, RpcException exception){
        long endTime = System.currentTimeMillis();
        MessageBody messageBody = SerializationUtils.deserialize(message.getBody(), MessageBody.class);
        String errMsg = exception.getMessage();
        Long logId = messageBody.getLogId();
        Long startTime = messageBody.getStartTime();

        OpenJobLogDO openJobLogDO = new OpenJobLogDO();
        openJobLogDO.setId(logId);
        openJobLogDO.setServerId(exception.getServerId());
        openJobLogDO.setAppId(messageBody.getAppId());
        openJobLogDO.setJobId(messageBody.getJobId());
        LocalDateTime dateTime = LocalDateTimeUtil.toLocalDateTime(startTime);
        openJobLogDO.setCreateTime(dateTime);
        openJobLogDO.setStartTime(dateTime);
        openJobLogDO.setStatus(JobStatusEnum.FAILED.getValue());
        openJobLogDO.setCause(errMsg);
        openJobLogDO.setFinishTime(LocalDateTimeUtil.toLocalDateTime(endTime));
        openJobLogDO.setTakeTime(endTime - startTime);
        JobLogEvent logEvent = new JobLogEvent(this, openJobLogDO);
        eventPublisher.publishEvent(logEvent);
    }
}
