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

package com.openbytecode.job.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openbytecode.job.admin.convert.OpenJobPromptConvert;
import com.openbytecode.job.admin.entity.OpenJobPrompt;
import com.openbytecode.job.admin.mapper.OpenJobPromptMapper;
import com.openbytecode.job.admin.service.OpenJobPromptService;
import com.openbytecode.job.api.dto.resp.OpenJobPromptRespDTO;
import com.openbytecode.job.api.dto.update.OpenJobPromptUpdateDTO;
import com.openbytecode.starter.security.context.UserSecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OpenJobPromptServiceImpl extends ServiceImpl<OpenJobPromptMapper, OpenJobPrompt> implements OpenJobPromptService {

    private final OpenJobPromptMapper promptMapper;

    public OpenJobPromptServiceImpl(OpenJobPromptMapper promptMapper) {
        this.promptMapper = promptMapper;
    }

    @Override
    public OpenJobPromptRespDTO queryPrompt() {
        OpenJobPrompt prompt = promptMapper.getOne();
        return OpenJobPromptConvert.INSTANCE.convert(prompt);
    }

    @Override
    public void updateById(OpenJobPromptUpdateDTO updateDTO) {
        OpenJobPrompt prompt = OpenJobPromptConvert.INSTANCE.convert(updateDTO);
        prompt.setUpdateTime(LocalDateTime.now());
        prompt.setUpdateUser(UserSecurityContextHolder.getUserId());
        promptMapper.updateById(prompt);
    }
}
