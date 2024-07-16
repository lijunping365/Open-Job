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
 * 任务运行日志
 * 
 * @author lijunping
 * @email lijunping365@gmail.com
 * @date 2021-09-06 10:10:03
 */
@Data
@TableName("open_job_log")
public class OpenJobLogDO implements Serializable {
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
	 * 任务 id
	 */
	private Long jobId;
	/**
	 * 服务 id
	 */
	private String serverId;
	/**
	 * 任务执行状态（1 成功，0 失败）
	 */
	private Integer status;
	/**
	 * 任务失败原因
	 */
	private String cause;
	/**
	 * 任务日志创建时间
	 */
	private LocalDateTime createTime;
	/**
	 * 任务开始时间
	 */
	private LocalDateTime startTime;
	/**
	 * 任务结束时间
	 */
	private LocalDateTime finishTime;
	/**
	 * 任务用时，单位毫秒
	 */
	private Long takeTime;

}
