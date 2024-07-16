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
package com.openbytecode.job.admin.convert;


import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.openbytecode.job.api.dto.create.OpenJobCreateDTO;
import com.openbytecode.job.api.dto.resp.OpenJobRespDTO;
import com.openbytecode.job.api.dto.update.OpenJobUpdateDTO;
import com.openbytecode.job.admin.entity.OpenJobDO;

import java.util.List;

/**
 * 任务表
 *
 * @author lijunping
 * @email lijunping365@gmail.com
 * @date 2021-09-06 10:10:03
 */
@Mapper
public interface OpenJobConvert {

    OpenJobConvert INSTANCE = Mappers.getMapper(OpenJobConvert.class);

    OpenJobRespDTO convert(OpenJobDO OpenJobDO);

    OpenJobDO convert(OpenJobCreateDTO OpenJobCreateDTO);

    OpenJobDO convert(OpenJobUpdateDTO OpenJobUpdateDTO);

    List<OpenJobRespDTO> convertList(List<OpenJobDO> jobDOS);
}
