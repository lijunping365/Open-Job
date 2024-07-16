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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openbytecode.job.admin.convert.OpenJobAppConvert;
import com.openbytecode.job.admin.entity.OpenJobAppDO;
import com.openbytecode.job.admin.mapper.OpenJobAppMapper;
import com.openbytecode.job.admin.service.OpenJobAppService;
import com.openbytecode.job.common.vo.PageResult;
import com.openbytecode.job.api.dto.batch.BatchDTO;
import com.openbytecode.job.api.dto.create.OpenJobAppCreateDTO;
import com.openbytecode.job.api.dto.req.OpenJobAppReqDTO;
import com.openbytecode.job.api.dto.resp.OpenJobAppRespDTO;
import com.openbytecode.job.api.dto.update.OpenJobAppUpdateDTO;
import com.openbytecode.starter.security.context.UserSecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class OpenJobAppServiceImpl extends ServiceImpl<OpenJobAppMapper, OpenJobAppDO> implements OpenJobAppService {

    private final OpenJobAppMapper openJobAppMapper;

    public OpenJobAppServiceImpl(OpenJobAppMapper openJobAppMapper) {
        this.openJobAppMapper = openJobAppMapper;
    }

    @Override
    public PageResult<OpenJobAppRespDTO> selectPage(OpenJobAppReqDTO openJobAppReqDTO) {
        Page<OpenJobAppDO> page = openJobAppMapper.queryPage(openJobAppReqDTO);
        IPage<OpenJobAppRespDTO> convert = page.convert(OpenJobAppConvert.INSTANCE::convert);
        return PageResult.build(convert);
    }

    @Override
    public OpenJobAppRespDTO getById(Long id) {
        OpenJobAppDO openJobAppDO = openJobAppMapper.selectById(id);
        return OpenJobAppConvert.INSTANCE.convert(openJobAppDO);
    }

    @Override
    public void save(OpenJobAppCreateDTO openJobAppCreateDTO) {
        OpenJobAppDO openJobAppDO = OpenJobAppConvert.INSTANCE.convert(openJobAppCreateDTO);
        openJobAppDO.setCreateTime(LocalDateTime.now());
        openJobAppDO.setCreateUser(UserSecurityContextHolder.getUserId());
        openJobAppMapper.insert(openJobAppDO);
    }

    @Override
    public void updateById(OpenJobAppUpdateDTO openJobAppUpdateDTO) {
        OpenJobAppDO openJobAppDO = OpenJobAppConvert.INSTANCE.convert(openJobAppUpdateDTO);
        openJobAppDO.setUpdateTime(LocalDateTime.now());
        openJobAppDO.setUpdateUser(UserSecurityContextHolder.getUserId());
        openJobAppMapper.updateById(openJobAppDO);
    }

    @Override
    public void deleteBatchIds(BatchDTO batchDTO) {
        openJobAppMapper.deleteBatchIds(batchDTO.getIds());
    }

    @Override
    public List<OpenJobAppRespDTO> queryList(String appName) {
        List<OpenJobAppDO> openJobAppDOS = openJobAppMapper.queryList(appName);
        return OpenJobAppConvert.INSTANCE.convertList(openJobAppDOS);
    }
}