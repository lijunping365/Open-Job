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
package com.openbytecode.job.admin.domain;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lijunping on 2022/4/25
 */
@Data
public class AlarmMessage implements Serializable {

    private static final long serialVersionUID = 8540414931056416275L;

    /**
     * 应用 id
     */
    private Long appId;
    /**
     * 任务 id
     */
    private Long jobId;

    /**
     * 执行器 id
     */
    private String serverId;

    /**
     * 任务执行失败原因
     */
    private String cause;

    /**
     * 任务开始执行时间
     */
    private LocalDateTime startTime;

    /**
     * 任务执行结束时间
     */
    private LocalDateTime finishTime;

    /**
     * 任务执行耗时
     */
    private Long takeTime;
}
