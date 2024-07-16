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
@TableName("open_job_instance")
public class OpenJobInstanceDO implements Serializable {
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
     * ip 地址
     */
    private String address;
    /**
     * 端口
     */
    private Integer port;
    /**
     * 上下线状态
     */
    private Integer status;
    /**
     * 权重
     */
    private Integer weight;
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
