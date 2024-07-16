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
package com.openbytecode.job.api.dto.resp;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author: 李俊平
 * @Date: 2022-02-26 14:56
 */
@Data
public class OpenJobInstanceRespDTO implements Serializable {

    private static final long serialVersionUID = -822276416137575995L;
    /**
     * id
     */
    private Long id;
    /**
     * 上下线状态
     */
    private Integer status;
    /**
     * 客户端地址：端口
     */
    private String serverId;
    /**
     * 上线时间
     */
    private LocalDateTime onlineTime;
    /**
     * 运行时长
     */
    private String liveTime;
    /**
     * cpu使用信息
     */
    private String cpuInfo;
    /**
     * 内存使用信息
     */
    private String memoryInfo;
    /**
     * 磁盘使用信息
     */
    private String diskInfo;
    /**
     * 权重
     */
    private Integer weight = 1;
}
