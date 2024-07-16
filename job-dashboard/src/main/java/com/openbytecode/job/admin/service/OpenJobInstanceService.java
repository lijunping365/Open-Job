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
import com.openbytecode.job.api.dto.create.OpenInstanceCreateDTO;
import com.openbytecode.job.api.dto.req.OpenJobInstanceReqDTO;
import com.openbytecode.job.api.dto.resp.OpenJobInstanceRespDTO;
import com.openbytecode.job.api.dto.update.OpenJobInstanceUpdateDTO;
import com.openbytecode.job.common.vo.PageResult;
import com.openbytecode.rpc.core.information.ServerInformation;

import java.util.List;

/**
 * @author: 李俊平
 * @Date: 2022-02-26 15:05
 */
public interface OpenJobInstanceService {
    
    PageResult<OpenJobInstanceRespDTO> selectPage(OpenJobInstanceReqDTO instanceReqDTO);

    OpenJobInstanceRespDTO getInstanceById(Long appId, String serverId);

    void updateServerStatus(OpenJobInstanceUpdateDTO updateDTO);

    List<OpenJobInstanceRespDTO> getInstanceList(Long appId);

    List<ServerInformation> lookup(String namespace);

    void deleteBatchIds(BatchDTO batchDTO);

    void add(OpenInstanceCreateDTO createDTO);
}
