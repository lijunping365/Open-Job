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

import com.openbytecode.rpc.core.transport.MessageResponseBody;
import com.openbytecode.rpc.server.callback.ResponseWriter;
import com.openbytecode.starter.executor.per.ThreadQueueNode;
import com.openbytecode.starter.job.register.core.OpenJobHandler;
import com.openbytecode.starter.job.register.param.HandlerParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: 李俊平
 * @Date: 2023-07-29 10:34
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobThreadQueueNode extends ThreadQueueNode {

    /**
     * 日志 id
     */
    private Long logId;

    /**
     * 任务开始时间
     */
    private long logDateTime;

    /**
     * 任务超时时间
     */
    private Integer executorTimeout;

    /**
     * 调度任务参数
     */
    private JobHandlerParam jobParam;

    /**
     * 响应回调
     */
    private ResponseWriter responseWriter;

    /**
     * 响应体
     */
    private MessageResponseBody responseBody;

    /**
     * 调度任务处理器
     */
    private OpenJobHandler<HandlerParam> handler;
}
