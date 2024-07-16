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

import java.time.LocalDateTime;

/**
 * @author lijunping on 2022/4/11
 */
@Data
@TableName("open_job_report")
public class OpenJobReportDO {

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 任务 id
     */
    private Long jobId;
    /**
     * 任务执行总次数
     */
    private Integer taskExecTotalCount;
    /**
     * 任务执行成功总次数
     */
    private Integer taskExecSuccessCount;
    /**
     * 任务配置创建时间
     */
    private LocalDateTime createTime;

}
