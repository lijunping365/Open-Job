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
package com.openbytecode.job.admin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("open_job")
public class OpenJobDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 应用 id
     */
    private Long appId;
    /**
     * 任务的名字
     */
    private String jobName;
    /**
     * 绑定的 handler 的名字
     */
    private String handlerName;
    /**
     * cron 表达式
     */
    private String cronExpression;
    /**
     * 任务参数 json 字符串
     */
    private String params;
    /**
     * base 脚本语言
     */
    private String scriptLang;
    /**
     * base 脚本
     */
    private String script;
    /**
     * 任务执行状态（1 启动，0 停止）
     */
    private Integer status;
    /**
     * 任务执行方式 0：采用负载均衡， 1：分片执行
     */
    private Integer sharding;
    /**
     * 执行超时时间
     */
    private Integer executorTimeout;
    /**
     * 任务创建时间
     */
    private LocalDateTime createTime;
    /**
     * 任务更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 任务创建人
     */
    private Long createUser;
    /**
     * 任务更新人
     */
    private Long updateUser;

}
