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

import com.openbytecode.job.admin.service.OpenJobInstanceService;
import com.openbytecode.job.api.dto.batch.BatchDTO;
import com.openbytecode.job.api.dto.create.OpenInstanceCreateDTO;
import com.openbytecode.job.api.dto.req.OpenJobInstanceReqDTO;
import com.openbytecode.job.api.dto.resp.OpenJobInstanceRespDTO;
import com.openbytecode.job.api.dto.update.OpenJobInstanceUpdateDTO;
import com.openbytecode.job.common.vo.PageResult;
import com.openbytecode.job.common.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 集群节点管理端点
 *
 * @author lijunping on 2022/2/16
 */
@Validated
@RestController
@RequestMapping("/instance")
public class OpenJobInstanceController {

    @Autowired
    private OpenJobInstanceService instanceService;


    @GetMapping("/page")
    public Result<PageResult<OpenJobInstanceRespDTO>> getPage(OpenJobInstanceReqDTO instanceReqDTO){
        return Result.succeed(instanceService.selectPage(instanceReqDTO));
    }

    @PostMapping("/add")
    public Result save(@RequestBody @Valid OpenInstanceCreateDTO createDTO) {
        instanceService.add(createDTO);
        return Result.succeed();
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestBody @Valid BatchDTO batchDTO) {
        instanceService.deleteBatchIds(batchDTO);
        return Result.succeed();
    }

    @GetMapping("/list")
    public Result<List<OpenJobInstanceRespDTO>> getAllInstance(@RequestParam("appId") Long appId){
        return Result.succeed(instanceService.getInstanceList(appId));
    }


    @PutMapping("/update")
    public Result offlineInstance(@RequestBody @Valid OpenJobInstanceUpdateDTO updateDTO){
        instanceService.updateServerStatus(updateDTO);
        return Result.succeed();
    }
}