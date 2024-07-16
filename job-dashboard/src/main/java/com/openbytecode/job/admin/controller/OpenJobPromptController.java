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
package com.openbytecode.job.admin.controller;

import com.openbytecode.job.admin.service.OpenJobPromptService;
import com.openbytecode.job.common.vo.Result;
import com.openbytecode.job.api.dto.resp.OpenJobPromptRespDTO;
import com.openbytecode.job.api.dto.update.OpenJobPromptUpdateDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author: 李俊平
 * @Date: 2023-12-30 09:54
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/prompt")
public class OpenJobPromptController {

    @Autowired
    private OpenJobPromptService openJobPromptService;

    @GetMapping("/query")
    public Result<OpenJobPromptRespDTO> query(){
        return Result.succeed(openJobPromptService.queryPrompt());
    }

    @PutMapping("/update")
    public Result update(@RequestBody @Valid OpenJobPromptUpdateDTO updateDTO) {
        openJobPromptService.updateById(updateDTO);
        return Result.succeed();
    }
}
