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
import com.openbytecode.job.admin.convert.OpenJobLogConvert;
import com.openbytecode.job.admin.entity.OpenJobLogDO;
import com.openbytecode.job.admin.mapper.OpenJobLogMapper;
import com.openbytecode.job.admin.service.OpenJobClientService;
import com.openbytecode.job.admin.service.OpenJobLogService;
import com.openbytecode.job.api.dto.batch.BatchDTO;
import com.openbytecode.job.api.dto.req.OpenJobLogReqDTO;
import com.openbytecode.job.api.dto.resp.OpenJobLogRespDTO;
import com.openbytecode.job.common.enums.JobStatusEnum;
import com.openbytecode.job.common.exception.ServiceException;
import com.openbytecode.job.common.log.JobLogResult;
import com.openbytecode.job.common.vo.PageResult;
import com.openbytecode.job.common.vo.ResultEnum;
import org.springframework.stereotype.Service;


@Service
public class OpenJobLogServiceImpl extends ServiceImpl<OpenJobLogMapper, OpenJobLogDO> implements OpenJobLogService {

    private final OpenJobClientService openJobClientService;
    private final OpenJobLogMapper openJobLogMapper;

    public OpenJobLogServiceImpl(OpenJobClientService openJobClientService, OpenJobLogMapper openJobLogMapper) {
        this.openJobClientService = openJobClientService;
        this.openJobLogMapper = openJobLogMapper;
    }

    @Override
    public PageResult<OpenJobLogRespDTO> selectPage(OpenJobLogReqDTO OpenJobLogReqDTO) {
        Page<OpenJobLogDO> page = openJobLogMapper.queryPage(OpenJobLogReqDTO);
        IPage<OpenJobLogRespDTO> convert = page.convert(OpenJobLogConvert.INSTANCE::convert);
        return PageResult.build(convert);
    }

    @Override
    public OpenJobLogRespDTO getById(Long id) {
        OpenJobLogDO OpenJobLogDO = openJobLogMapper.selectById(id);
        return OpenJobLogConvert.INSTANCE.convert(OpenJobLogDO);
    }

    @Override
    public void kill(Long id) {
        OpenJobLogDO openJobLogDO = openJobLogMapper.selectById(id);
        if (JobStatusEnum.of(openJobLogDO.getStatus()) != JobStatusEnum.RUNNING){
            throw new ServiceException(ResultEnum.TASK_COMPLETED);
        }
        openJobClientService.killTask(openJobLogDO.getJobId(), openJobLogDO.getServerId());
    }

    @Override
    public JobLogResult catLog(Long id, Integer fromLineNum) {
        OpenJobLogDO openJobLogDO = openJobLogMapper.selectById(id);
        return openJobClientService.catLog(openJobLogDO,fromLineNum);
    }

    @Override
    public void deleteBatchIds(BatchDTO batchDTO) {
        openJobLogMapper.deleteBatchIds(batchDTO.getIds());
    }
}