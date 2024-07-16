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
import com.openbytecode.job.api.dto.req.OpenJobUserReqDTO;
import com.openbytecode.job.api.dto.create.OpenJobUserCreateDTO;
import com.openbytecode.job.api.dto.resp.OpenJobUserRespDTO;
import com.openbytecode.job.api.dto.update.OpenJobUserUpdateDTO;
import com.openbytecode.job.common.vo.PageResult;

/**
 * 用户表
 *
 * @author lijunping
 * @email lijunping365@gmail.com
 * @date 2021-06-22 15:20:30
 */
public interface OpenJobUserService {

    PageResult<OpenJobUserRespDTO> selectPage(OpenJobUserReqDTO openJobUserReqDTO);

    OpenJobUserRespDTO getById(Long id);

    void save(OpenJobUserCreateDTO openJobUserCreateDTO);

    void updateById(OpenJobUserUpdateDTO openJobUserUpdateDTO);

    void deleteBatchIds(BatchDTO batchDTO);

    OpenJobUserRespDTO loadUserByUserId(Long userId);
}

