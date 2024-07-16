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
package com.openbytecode.job.admin.service;

import com.openbytecode.job.api.dto.batch.BatchDTO;
import com.openbytecode.job.api.dto.req.OpenJobReqDTO;
import com.openbytecode.job.api.dto.create.OpenJobCreateDTO;
import com.openbytecode.job.api.dto.resp.OpenJobRespDTO;
import com.openbytecode.job.api.dto.resp.OpenJobTriggerTimeDTO;
import com.openbytecode.job.api.dto.update.OpenJobUpdateDTO;
import com.openbytecode.job.common.vo.PageResult;

import java.util.List;

/**
 * 任务表
 *
 * @author lijunping
 * @email lijunping365@gmail.com
 * @date 2021-09-06 10:10:03
 */
public interface OpenJobService {

    PageResult<OpenJobRespDTO> selectPage(OpenJobReqDTO OpenJobReqDTO);

    OpenJobRespDTO getById(Long id);

    void save(OpenJobCreateDTO OpenJobCreateDTO);

    void updateById(OpenJobUpdateDTO OpenJobUpdateDTO);

    void start(Long id);

    void stop(Long id);

    void deleteBatchIds(BatchDTO batchDTO);

    void run(Long id);

    OpenJobTriggerTimeDTO nextTriggerTime(String cronExpress, Integer count);

    void validateCron(String cronExpress);

    List<OpenJobRespDTO> fetchByJobName(String jobName);
}
