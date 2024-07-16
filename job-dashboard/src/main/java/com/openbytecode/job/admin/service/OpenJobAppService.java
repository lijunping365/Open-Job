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
import com.openbytecode.job.api.dto.create.OpenJobAppCreateDTO;
import com.openbytecode.job.api.dto.req.OpenJobAppReqDTO;
import com.openbytecode.job.api.dto.resp.OpenJobAppRespDTO;
import com.openbytecode.job.api.dto.update.OpenJobAppUpdateDTO;
import com.openbytecode.job.common.vo.PageResult;

import java.util.List;

/**
 * 任务运行日志
 *
 * @author lijunping
 * @email lijunping365@gmail.com
 * @date 2021-09-06 10:10:03
 */
public interface OpenJobAppService {

    PageResult<OpenJobAppRespDTO> selectPage(OpenJobAppReqDTO openJobAppReqDTO);

    OpenJobAppRespDTO getById(Long id);

    void save(OpenJobAppCreateDTO openJobAppCreateDTO);

    void updateById(OpenJobAppUpdateDTO openJobAppUpdateDTO);

    void deleteBatchIds(BatchDTO batchDTO);

    List<OpenJobAppRespDTO> queryList(String appName);
}

