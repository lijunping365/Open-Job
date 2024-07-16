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
package com.openbytecode.job.common.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: 李俊平
 * @Date: 2022-03-05 11:50
 */
@Data
public class MessageBody implements Serializable {
    private static final long serialVersionUID = 8230301428590315404L;
    /**
     * The id of App
     */
    private Long appId;
    /**
     * The id of job
     */
    private Long jobId;
    /**
     * The id of job log
     */
    private Long logId;
    /**
     * The params of handler
     */
    private String params;
    /**
     * The script of bash
     */
    private String script;
    /**
     * The message command
     */
    private String command;
    /**
     * The task startTime
     */
    private Long startTime;
    /**
     * base 脚本语言
     */
    private String scriptLang;
    /**
     * The number of log
     */
    private Integer fromLineNum;
    /**
     * The timeout of task
     */
    private Integer executorTimeout;
    /**
     * The name of JobHandler
     */
    private String handlerName;
    /**
     * The update time of script
     */
    private String scriptUpdateTime;
    /**
     * The shardingNodes
     */
    private List<String> shardingNodes;
}
