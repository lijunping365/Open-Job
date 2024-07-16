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
package com.openbytecode.job.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.openbytecode.job.admin.entity.OpenJobPrompt;
import org.springframework.stereotype.Repository;

/**
 * 提示词表
 *
 * @author lijunping
 */
@Repository
public interface OpenJobPromptMapper extends BaseMapper<OpenJobPrompt> {

    default OpenJobPrompt getOne(){
        return selectOne(Wrappers.<OpenJobPrompt>lambdaQuery()
                .last("limit 1")
        );
    }
}
