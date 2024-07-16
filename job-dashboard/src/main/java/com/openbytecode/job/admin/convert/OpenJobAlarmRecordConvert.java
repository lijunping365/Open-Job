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

import com.openbytecode.job.admin.entity.OpenJobAlarmRecordDO;
import com.openbytecode.job.api.dto.create.OpenJobAlarmRecordCreateDTO;
import com.openbytecode.job.api.dto.resp.OpenJobAlarmRecordRespDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author: 李俊平
 * @Date: 2023-05-25 07:43
 */
@Mapper
public interface OpenJobAlarmRecordConvert {

    OpenJobAlarmRecordConvert INSTANCE = Mappers.getMapper(OpenJobAlarmRecordConvert.class);

    OpenJobAlarmRecordRespDTO convert(OpenJobAlarmRecordDO OpenJobAlarmRecordDO);

    OpenJobAlarmRecordDO convert(OpenJobAlarmRecordCreateDTO OpenJobAlarmRecordCreateDTO);
}
