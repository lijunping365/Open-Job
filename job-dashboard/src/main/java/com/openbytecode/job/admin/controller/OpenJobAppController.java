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

import com.openbytecode.job.admin.service.OpenJobAppService;
import com.openbytecode.job.api.dto.batch.BatchDTO;
import com.openbytecode.job.api.dto.create.OpenJobAppCreateDTO;
import com.openbytecode.job.api.dto.req.OpenJobAppReqDTO;
import com.openbytecode.job.api.dto.resp.OpenJobAppRespDTO;
import com.openbytecode.job.api.dto.update.OpenJobAppUpdateDTO;
import com.openbytecode.job.common.vo.PageResult;
import com.openbytecode.job.common.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * app
 *
 * @author lijunping
 * @email lijunping365@gmail.com
 * @date 2021-09-06 10:10:03
 */
@Validated
@RestController
@RequestMapping("/app")
public class OpenJobAppController {

  @Autowired
  private OpenJobAppService openJobAppService;

  @GetMapping("/page")
  public Result<PageResult<OpenJobAppRespDTO>> page(OpenJobAppReqDTO openJobAppReqDTO) {
    return Result.succeed(openJobAppService.selectPage(openJobAppReqDTO));
  }

  @GetMapping("/list")
  public Result<List<OpenJobAppRespDTO>> list(@RequestParam(required = false) String appName) {
    return Result.succeed(openJobAppService.queryList(appName));
  }

  @GetMapping("/info/{id}")
  public Result<OpenJobAppRespDTO> info(@PathVariable("id") Long id) {
    return Result.succeed(openJobAppService.getById(id));
  }

  @PostMapping("/save")
  public Result save(@RequestBody @Valid OpenJobAppCreateDTO openJobAppCreateDTO) {
    openJobAppService.save(openJobAppCreateDTO);
    return Result.succeed();
  }

  @PutMapping("/update")
  public Result update(@RequestBody @Valid OpenJobAppUpdateDTO openJobAppUpdateDTO) {
    openJobAppService.updateById(openJobAppUpdateDTO);
    return Result.succeed();
  }

  @DeleteMapping("/delete")
  public Result delete(@RequestBody @Valid BatchDTO batchDTO) {
    openJobAppService.deleteBatchIds(batchDTO);
    return Result.succeed();
  }

}
