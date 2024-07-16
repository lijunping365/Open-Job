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

import com.openbytecode.job.admin.service.OpenJobService;
import com.openbytecode.job.api.dto.batch.BatchDTO;
import com.openbytecode.job.api.dto.create.OpenJobCreateDTO;
import com.openbytecode.job.api.dto.req.OpenJobReqDTO;
import com.openbytecode.job.api.dto.resp.OpenJobRespDTO;
import com.openbytecode.job.api.dto.resp.OpenJobTriggerTimeDTO;
import com.openbytecode.job.api.dto.update.OpenJobUpdateDTO;
import com.openbytecode.job.common.vo.PageResult;
import com.openbytecode.job.common.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 任务表
 *
 * @author lijunping
 * @email lijunping365@gmail.com
 * @date 2021-09-06 10:10:03
 */
@Validated
@RestController
@RequestMapping("/task")
public class OpenJobController {

    @Autowired
    private OpenJobService openJobService;

    @GetMapping("/page")
    public Result<PageResult<OpenJobRespDTO>> page(OpenJobReqDTO OpenJobReqDTO) {
        return Result.succeed(openJobService.selectPage(OpenJobReqDTO));
    }

    @GetMapping("/info/{id}")
    public Result<OpenJobRespDTO> info(@PathVariable("id") Long id) {
        return Result.succeed(openJobService.getById(id));
    }

    @GetMapping("/nextTriggerTime")
    public Result<OpenJobTriggerTimeDTO> nextTriggerTime(@RequestParam("cronExpress") String cronExpress,
                                                         @RequestParam(value = "count", required = false, defaultValue = "5") Integer count){
        return Result.succeed(openJobService.nextTriggerTime(cronExpress, count));
    }

    @PostMapping("/save")
    public Result save(@RequestBody @Valid OpenJobCreateDTO openJobCreateDTO) {
        openJobService.save(openJobCreateDTO);
        return Result.succeed();
    }

    @PutMapping("/update")
    public Result update(@RequestBody @Valid OpenJobUpdateDTO OpenJobUpdateDTO) {
        openJobService.updateById(OpenJobUpdateDTO);
        return Result.succeed();
    }

    @GetMapping("/validateCron")
    public Result validateCron(@RequestParam("cronExpress") String cronExpress){
        openJobService.validateCron(cronExpress);
        return Result.succeed();
    }

    @GetMapping("/fetchByJobName")
    public Result<List<OpenJobRespDTO>> fetchByJobName(@RequestParam("jobName") String jobName){
        return Result.succeed(openJobService.fetchByJobName(jobName));
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestBody @Valid BatchDTO batchDTO) {
        openJobService.deleteBatchIds(batchDTO);
        return Result.succeed();
    }

    @PutMapping("/start/{id}")
    public Result start(@PathVariable("id") Long id) {
        openJobService.start(id);
        return Result.succeed();
    }

    @PutMapping("/stop/{id}")
    public Result stop(@PathVariable("id") Long id) {
        openJobService.stop(id);
        return Result.succeed();
    }

    @PutMapping("/run/{id}")
    public Result run(@PathVariable("id") Long id) {
        openJobService.run(id);
        return Result.succeed();
    }
}
