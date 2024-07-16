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
package com.openbytecode.job.sample.components;

import com.openbytecode.job.common.domain.JobHandlerEntity;
import com.openbytecode.job.common.domain.MessageBody;
import com.openbytecode.job.common.domain.ResponseBody;
import com.openbytecode.job.common.enums.CommandEnum;
import com.openbytecode.job.common.json.JSON;
import com.openbytecode.job.common.log.JobLogResult;
import com.openbytecode.job.common.metrics.SystemMetricsInfo;
import com.openbytecode.job.common.metrics.SystemMetricsUtils;
import com.openbytecode.job.common.serialize.SerializationUtils;
import com.openbytecode.job.common.time.LocalDateTimeUtil;
import com.openbytecode.job.sample.utils.LogUtils;
import com.openbytecode.rpc.core.Message;
import com.openbytecode.rpc.core.enums.ResponseStatus;
import com.openbytecode.rpc.core.exception.RpcException;
import com.openbytecode.rpc.core.transport.MessageResponseBody;
import com.openbytecode.rpc.server.callback.ResponseWriter;
import com.openbytecode.rpc.server.process.AbstractMessageProcess;
import com.openbytecode.starter.executor.per.TaskThread;
import com.openbytecode.starter.executor.per.TaskThreadHolder;
import com.openbytecode.starter.job.register.annotation.JobHandler;
import com.openbytecode.starter.job.register.core.JobHandlerHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author lijunping on 2022/2/25
 */
@Slf4j
@Component
public class JobMessageProcessor extends AbstractMessageProcess {

    private final JobTaskProcessor jobTaskProcessor;
    private final ThreadPoolExecutor threadPoolExecutor;
    private final JobHandlerHolder jobHandlerHolder;

    public JobMessageProcessor(JobTaskProcessor jobTaskProcessor,
                               ThreadPoolExecutor threadPoolExecutor,
                               JobHandlerHolder jobHandlerHolder){
        this.jobTaskProcessor = jobTaskProcessor;
        this.threadPoolExecutor = threadPoolExecutor;
        this.jobHandlerHolder = jobHandlerHolder;
    }

    @Override
    public void doProcess(Message message, MessageResponseBody responseBody, ResponseWriter responseWriter) {
        threadPoolExecutor.execute(()->processAsync(message, responseBody, responseWriter));
    }

    private void processAsync(Message message, MessageResponseBody responseBody, ResponseWriter responseWriter){
        final byte[] body = message.getBody();
        final MessageBody messageBody = SerializationUtils.deserialize(body, MessageBody.class);
        CommandEnum command = CommandEnum.of(messageBody.getCommand());
        if (command == CommandEnum.SCHEDULE){
            jobTaskProcessor.handlerSchedule(messageBody, responseBody, responseWriter);
            return;
        }

        try {
            handlerMessage(messageBody, command, responseBody);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            responseBody.setMsg(e.getMessage());
            responseBody.setStatus(ResponseStatus.ERROR);
        } finally {
            responseWriter.write(responseBody);
        }
    }

    private void handlerMessage(MessageBody messageBody, CommandEnum command, MessageResponseBody response){
        if (Objects.isNull(command)) {
            throw new RpcException("the parameter command must not be null");
        }

        switch (command){
            case KILL:
                handlerKillTask(messageBody);
                break;
            case METRICS:
                ResponseBody metrics = handlerMetrics();
                response.setBody(SerializationUtils.serialize(metrics));
                break;
            case CAT_LOG:
                ResponseBody catLog = handlerCatLog(messageBody);
                response.setBody(SerializationUtils.serialize(catLog));
                break;
            case QUERY_HANDLER:
                ResponseBody handlers = handlerQueryHandler();
                response.setBody(SerializationUtils.serialize(handlers));
                break;
            default:
                throw new UnsupportedOperationException("Unsupported Operation");
        }
    }



    private void handlerKillTask(MessageBody messageBody){
        TaskThread taskThread = TaskThreadHolder.getThread(messageBody.getJobId());
        if (Objects.isNull(taskThread)){
            throw new RpcException("Can't find the task thread");
        }
        TaskThreadHolder.removeThread(messageBody.getJobId());
    }

    private ResponseBody handlerCatLog(MessageBody messageBody){
        ResponseBody responseBody = new ResponseBody();
        LocalDateTime triggerDate = LocalDateTimeUtil.toLocalDateTime(messageBody.getStartTime());
        String logFileName = LogUtils.makeLogFileName(triggerDate, messageBody.getLogId());
        JobLogResult logResult = LogUtils.readLog(logFileName, messageBody.getFromLineNum());
        responseBody.setData(JSON.toJSON(logResult));
        return responseBody;
    }

    private ResponseBody handlerQueryHandler(){
        ResponseBody responseBody = new ResponseBody();
        List<JobHandlerEntity> jobHandlerEntities = new ArrayList<>();
        List<JobHandler> annotations = jobHandlerHolder.getAllJobHandlerAnnotation();
        for (JobHandler annotation : annotations) {
            JobHandlerEntity entity = new JobHandlerEntity();
            entity.setValue(annotation.value());
            entity.setName(annotation.name());
            jobHandlerEntities.add(entity);
        }
        responseBody.setData(JSON.toJSON(jobHandlerEntities));
        return responseBody;
    }

    private ResponseBody handlerMetrics(){
        ResponseBody responseBody = new ResponseBody();
        SystemMetricsInfo systemMetricsInfo = new SystemMetricsInfo();
        int cpuProcessorNum = SystemMetricsUtils.getCPUProcessorNum();
        double cpuLoadPercent = SystemMetricsUtils.getCPULoadPercent();
        double jvmUsedMemory = SystemMetricsUtils.getJvmUsedMemory();
        double jvmMaxMemory = SystemMetricsUtils.getJvmMaxMemory();
        double jvmMemoryUsage = SystemMetricsUtils.getJvmMemoryUsage(jvmUsedMemory, jvmMaxMemory);
        long[] diskInfo = SystemMetricsUtils.getDiskInfo();
        long freeDiskSpace = diskInfo[0];
        long totalDiskSpace = diskInfo[1];
        double diskUsed = SystemMetricsUtils.getDiskUsed(totalDiskSpace, freeDiskSpace);
        double diskTotal = SystemMetricsUtils.getDiskTotal(totalDiskSpace);
        double diskUsage = SystemMetricsUtils.getDiskUsage(diskUsed, diskTotal);

        systemMetricsInfo.setCpuProcessors(cpuProcessorNum);
        systemMetricsInfo.setCpuLoad(cpuLoadPercent);
        systemMetricsInfo.setJvmMaxMemory(jvmMaxMemory);
        systemMetricsInfo.setJvmUsedMemory(jvmUsedMemory);
        systemMetricsInfo.setJvmMemoryUsage(jvmMemoryUsage);
        systemMetricsInfo.setDiskUsed(diskUsed);
        systemMetricsInfo.setDiskTotal(diskTotal);
        systemMetricsInfo.setDiskUsage(diskUsage);

        responseBody.setData(JSON.toJSON(systemMetricsInfo));
        return responseBody;
    }


}
