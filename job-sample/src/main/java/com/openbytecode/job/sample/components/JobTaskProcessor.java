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

import com.openbytecode.job.common.constants.CommonConstant;
import com.openbytecode.job.common.domain.MessageBody;
import com.openbytecode.job.common.time.LocalDateTimeUtil;
import com.openbytecode.job.sample.utils.LogUtils;
import com.openbytecode.rpc.core.enums.ResponseStatus;
import com.openbytecode.rpc.core.transport.MessageResponseBody;
import com.openbytecode.rpc.server.callback.ResponseWriter;
import com.openbytecode.starter.executor.TaskExecutor;
import com.openbytecode.starter.job.register.core.JobHandlerHolder;
import com.openbytecode.starter.job.register.core.OpenJobHandler;
import com.openbytecode.starter.job.register.param.HandlerParam;
import com.openbytecode.starter.logger.LoggerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author: 李俊平
 * @Date: 2024-01-21 12:23
 */
@Slf4j
@Component
public class JobTaskProcessor {

    private final TaskExecutor taskExecutor;
    private final JobHandlerHolder jobHandlerHolder;

    public JobTaskProcessor(TaskExecutor taskExecutor, JobHandlerHolder jobHandlerHolder) {
        this.taskExecutor = taskExecutor;
        this.jobHandlerHolder = jobHandlerHolder;
    }

    public void handlerSchedule(MessageBody messageBody, MessageResponseBody responseBody, ResponseWriter responseWriter){
        String handlerName = messageBody.getHandlerName();
        if (StringUtils.isBlank(handlerName)){
            handlerName = CommonConstant.SCRIPT_HANDLER_NAME;
        }

        OpenJobHandler openJobHandler = jobHandlerHolder.getJobHandler(handlerName);
        if (ObjectUtils.isEmpty(openJobHandler)) {
            responseBody.setMsg("JobHandlerName: " + handlerName + ", there is no bound JobHandler.");
            responseBody.setStatus(ResponseStatus.ERROR);
            responseWriter.write(responseBody);
            return;
        }

        JobHandlerParam jobParam = JobHandlerParam.builder()
                .jobId(messageBody.getJobId())
                .params(messageBody.getParams())
                .script(messageBody.getScript())
                .scriptLang(messageBody.getScriptLang())
                .scriptUpdateTime(messageBody.getScriptUpdateTime())
                .shardingNodes(messageBody.getShardingNodes())
                .build();

        JobThreadQueueNode jobThreadQueueNode = JobThreadQueueNode.builder()
                .logId(messageBody.getLogId())
                .logDateTime(messageBody.getStartTime())
                .jobParam(jobParam)
                .handler(openJobHandler)
                .responseWriter(responseWriter)
                .executorTimeout(messageBody.getExecutorTimeout())
                .responseBody(responseBody)
                .build();
        jobThreadQueueNode.setTaskId(messageBody.getJobId());

        runTask(jobThreadQueueNode);
    }

    private void runTask(JobThreadQueueNode jobThreadQueueNode){
        taskExecutor.execute(jobThreadQueueNode, ((queueNode, isStop) -> {
            if (isStop){
                log.info("handle job in queue when taskThread is killed. jobId {}, and logId {}", queueNode.getTaskId(), queueNode.getLogId());
                ResponseWriter responseWriter = queueNode.getResponseWriter();
                MessageResponseBody responseBody = queueNode.getResponseBody();
                responseBody.setMsg("job not executed in the job queue, killed.");
                responseBody.setStatus(ResponseStatus.ERROR);
                responseWriter.write(responseBody);
            }else {
                execute(queueNode);
            }
        }));
    }

    /**
     * execute job
     * @param jobThreadQueueNode
     */
    private void execute(JobThreadQueueNode jobThreadQueueNode){
        Long jobId = jobThreadQueueNode.getJobParam().getJobId();
        ResponseWriter responseWriter = jobThreadQueueNode.getResponseWriter();
        MessageResponseBody responseBody = jobThreadQueueNode.getResponseBody();
        try {
            doExecute(jobThreadQueueNode);
        }catch (Throwable e){
            responseBody.setMsg("Job : " + jobId + ", execute exception:" + e.getMessage());
            responseBody.setStatus(ResponseStatus.ERROR);
        }finally {
            responseWriter.write(responseBody);
        }
    }

    private void doExecute(JobThreadQueueNode jobThreadQueueNode) throws Exception {
        JobHandlerParam jobParam = jobThreadQueueNode.getJobParam();
        OpenJobHandler<HandlerParam> handler = jobThreadQueueNode.getHandler();

        LocalDateTime triggerDate = LocalDateTimeUtil.toLocalDateTime(jobThreadQueueNode.getLogDateTime());
        String logFileName = LogUtils.makeLogFileName(triggerDate, jobThreadQueueNode.getLogId());
        LoggerContext loggerContext = new LoggerContext(logFileName);
        LoggerContext.setLoggerContext(loggerContext);

        Integer executorTimeout = jobThreadQueueNode.getExecutorTimeout();
        if (Objects.isNull(executorTimeout) || executorTimeout <= 0){
            handler.handler(jobParam);
            return;
        }

        FutureTask<Boolean> futureTask = new FutureTask<>(() -> {
            LoggerContext.setLoggerContext(loggerContext);
            handler.handler(jobParam);
            return true;
        });

        Thread futureThread = new Thread(futureTask);
        futureThread.start();

        try {
            futureTask.get(executorTimeout, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            throw new RuntimeException("job execute timeout");
        } finally {
            futureThread.interrupt();
        }
    }
}
