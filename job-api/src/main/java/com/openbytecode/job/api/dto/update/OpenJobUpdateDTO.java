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
package com.openbytecode.job.api.dto.update;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 任务表
 *
 * @author lijunping
 * @email lijunping365@gmail.com
 * @date 2021-09-06 10:10:03
 */
@Data
public class OpenJobUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long appId;

    private String jobName;

    private String handlerName;

    private String cronExpression;

    private String params;

    private String script;

    private Integer status;

    private Integer sharding;

    private String scriptLang;

    private Integer executorTimeout;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;

}
