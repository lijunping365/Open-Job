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

import com.openbytecode.job.admin.service.OpenJobUserService;
import com.openbytecode.job.api.dto.batch.BatchDTO;
import com.openbytecode.job.api.dto.req.OpenJobUserReqDTO;
import com.openbytecode.job.common.vo.PageResult;
import com.openbytecode.job.common.vo.Result;
import com.openbytecode.starter.security.context.UserSecurityContextHolder;
import com.openbytecode.job.api.dto.create.OpenJobUserCreateDTO;
import com.openbytecode.job.api.dto.resp.OpenJobUserRespDTO;
import com.openbytecode.job.api.dto.update.OpenJobUserUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * 用户表
 *
 * @author lijunping
 * @email lijunping365@gmail.com
 * @date 2021-06-22 15:20:30
 */
@Validated
@RestController
@RequestMapping("/user")
public class OpenJobUserController {

  @Autowired
  private OpenJobUserService openJobUserService;

  @GetMapping("/currentUser")
  public Result<OpenJobUserRespDTO> getCurrentUser() {
    Long userId = UserSecurityContextHolder.getUserId();
    return Result.succeed(openJobUserService.loadUserByUserId(userId));
  }

  @GetMapping("/page")
  public Result<PageResult<OpenJobUserRespDTO>> page(OpenJobUserReqDTO openJobUserReqDTO) {
    return Result.succeed(openJobUserService.selectPage(openJobUserReqDTO));
  }

  @GetMapping("/info/{id}")
  public Result<OpenJobUserRespDTO> info(@PathVariable("id") Long id) {
    return Result.succeed(openJobUserService.getById(id));
  }

  @PostMapping("/save")
  public Result save(@RequestBody @Valid OpenJobUserCreateDTO openJobUserCreateDTO) {
    openJobUserService.save(openJobUserCreateDTO);
    return Result.succeed();
  }

  @PutMapping("/update")
  public Result update(@RequestBody @Valid OpenJobUserUpdateDTO openJobUserUpdateDTO) {
    openJobUserService.updateById(openJobUserUpdateDTO);
    return Result.succeed();
  }

  @DeleteMapping("/delete")
  public Result delete(@RequestBody @Valid BatchDTO batchDTO) {
    openJobUserService.deleteBatchIds(batchDTO);
    return Result.succeed();
  }

}
