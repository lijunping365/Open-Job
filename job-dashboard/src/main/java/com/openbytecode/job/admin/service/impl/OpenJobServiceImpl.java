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
import com.openbytecode.job.admin.convert.OpenJobConvert;
import com.openbytecode.job.admin.entity.OpenJobDO;
import com.openbytecode.job.admin.mapper.OpenJobMapper;
import com.openbytecode.job.admin.service.OpenJobService;
import com.openbytecode.job.common.enums.CommonStatusEnum;
import com.openbytecode.job.common.exception.ServiceException;
import com.openbytecode.job.common.time.LocalDateTimeUtil;
import com.openbytecode.job.common.vo.PageResult;
import com.openbytecode.job.api.dto.batch.BatchDTO;
import com.openbytecode.job.api.dto.create.OpenJobCreateDTO;
import com.openbytecode.job.api.dto.req.OpenJobReqDTO;
import com.openbytecode.job.api.dto.resp.OpenJobRespDTO;
import com.openbytecode.job.api.dto.resp.OpenJobTriggerTimeDTO;
import com.openbytecode.job.api.dto.update.OpenJobUpdateDTO;
import com.openbytecode.starter.schedule.cron.CronExpression;
import com.openbytecode.starter.schedule.domain.ScheduleTask;
import com.openbytecode.starter.schedule.executor.ScheduleTaskExecutor;
import com.openbytecode.starter.schedule.service.ScheduleTaskService;
import com.openbytecode.starter.security.context.UserSecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lijunping on 2022/2/17
 */
@Service
public class OpenJobServiceImpl extends ServiceImpl<OpenJobMapper, OpenJobDO> implements OpenJobService, ScheduleTaskService {

    private final OpenJobMapper openJobMapper;
    private final ScheduleTaskExecutor scheduleTaskExecutor;

    public OpenJobServiceImpl(OpenJobMapper openJobMapper, ScheduleTaskExecutor scheduleTaskExecutor) {
        this.openJobMapper = openJobMapper;
        this.scheduleTaskExecutor = scheduleTaskExecutor;
    }

    @Override
    public PageResult<OpenJobRespDTO> selectPage(OpenJobReqDTO openJobReqDTO) {
        Page<OpenJobDO> page = openJobMapper.queryPage(openJobReqDTO);
        IPage<OpenJobRespDTO> convert = page.convert(OpenJobConvert.INSTANCE::convert);
        return PageResult.build(convert);
    }

    @Override
    public OpenJobRespDTO getById(Long id) {
        OpenJobDO OpenJobDO = openJobMapper.selectById(id);
        return OpenJobConvert.INSTANCE.convert(OpenJobDO);
    }

    @Override
    public void save(OpenJobCreateDTO openJobCreateDTO) {
        String cronExpression = openJobCreateDTO.getCronExpression();
        if (!CronExpression.isValidExpression(cronExpression)){
            throw new ServiceException("Invalid cronExpression");
        }
        openJobCreateDTO.setCreateTime(LocalDateTime.now());
        openJobCreateDTO.setCreateUser(UserSecurityContextHolder.getUserId());
        openJobMapper.insert(OpenJobConvert.INSTANCE.convert(openJobCreateDTO));
    }

    @Override
    public void updateById(OpenJobUpdateDTO openJobUpdateDTO) {
        OpenJobDO openJobDO = openJobMapper.selectById(openJobUpdateDTO.getId());
        OpenJobDO convert = OpenJobConvert.INSTANCE.convert(openJobUpdateDTO);
        openJobDO.setUpdateTime(LocalDateTime.now());
        openJobMapper.updateById(convert);
    }

    @Override
    public void start(Long id) {
        OpenJobDO openJobDO = openJobMapper.selectById(id);
        openJobDO.setStatus(CommonStatusEnum.YES.getValue());
        openJobDO.setUpdateTime(LocalDateTime.now());
        openJobMapper.updateById(openJobDO);
    }

    @Override
    public void stop(Long id) {
        OpenJobDO openJobDO = openJobMapper.selectById(id);
        openJobDO.setStatus(CommonStatusEnum.NO.getValue());
        openJobDO.setUpdateTime(LocalDateTime.now());
        openJobMapper.updateById(openJobDO);
    }

    @Override
    public void deleteBatchIds(BatchDTO batchDTO) {
        List<Long> ids = batchDTO.getIds();
        openJobMapper.deleteBatchIds(ids);
    }

    @Override
    public void run(Long id) {
        scheduleTaskExecutor.execute(Collections.singletonList(id));
    }

    @Override
    public OpenJobTriggerTimeDTO nextTriggerTime(String cronExpress, Integer count) {
        OpenJobTriggerTimeDTO dto = new OpenJobTriggerTimeDTO();
        List<String> result = new ArrayList<>();
        String errMsg = null;
        try {
            result = genNextTimes(count, cronExpress);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        dto.setTimes(result);
        dto.setErrMsg(errMsg);
        return dto;
    }

    @Override
    public List<ScheduleTask> loadTask() {
        List<OpenJobDO> scheduleTasks = openJobMapper.queryStartJob();
        if (CollectionUtils.isEmpty(scheduleTasks)){
            return Collections.emptyList();
        }
        return scheduleTasks.stream().map(this::createScheduleTask).collect(Collectors.toList());
    }

    @Override
    public void validateCron(String cronExpress) {
        try {
            CronExpression.validateExpression(cronExpress);
        } catch (ParseException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<OpenJobRespDTO> fetchByJobName(String jobName) {
        List<OpenJobDO> openJobDOS = openJobMapper.fetchByJobName(jobName);
        return OpenJobConvert.INSTANCE.convertList(openJobDOS);
    }

    private ScheduleTask createScheduleTask(OpenJobDO openJobDO){
        ScheduleTask scheduleTask = new ScheduleTask();
        scheduleTask.setTaskId(openJobDO.getId());
        scheduleTask.setCronExpression(openJobDO.getCronExpression());
        return scheduleTask;
    }

    private List<String> genNextTimes(int size, String cronExpress) throws ParseException {
        List<String> result = new ArrayList<>();
        Date lastTime = new Date();
        for (int i = 0; i < size; i++) {
            lastTime = new CronExpression(cronExpress).getNextValidTimeAfter(lastTime);
            LocalDateTime localDateTime = LocalDateTimeUtil.convertDateToLDT(lastTime);
            String time = LocalDateTimeUtil.format(localDateTime, LocalDateTimeUtil.DATETIME_FORMATTER);
            result.add(time);
        }
        return result;
    }
}
