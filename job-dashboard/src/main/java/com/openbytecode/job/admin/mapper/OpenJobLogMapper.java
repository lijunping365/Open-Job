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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openbytecode.job.api.dto.req.OpenJobLogReqDTO;
import com.openbytecode.job.admin.entity.OpenJobLogDO;
import com.openbytecode.job.common.enums.CommonStatusEnum;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 任务运行日志
 * 
 * @author lijunping
 * @email lijunping365@gmail.com
 * @date 2021-09-06 10:10:03
 */
@Repository
public interface OpenJobLogMapper extends BaseMapper<OpenJobLogDO> {

    default Page<OpenJobLogDO> queryPage(OpenJobLogReqDTO openJobLogReqDTO){
        return selectPage(openJobLogReqDTO.page(), Wrappers.<OpenJobLogDO>lambdaQuery()
            .eq(Objects.nonNull(openJobLogReqDTO.getJobId()), OpenJobLogDO::getJobId, openJobLogReqDTO.getJobId())
            .eq(Objects.nonNull(openJobLogReqDTO.getStatus()), OpenJobLogDO::getStatus, openJobLogReqDTO.getStatus())
            .between(Objects.nonNull(openJobLogReqDTO.getBeginTime()), OpenJobLogDO::getCreateTime, openJobLogReqDTO.getBeginTime(), openJobLogReqDTO.getEndTime())
            .orderByDesc(OpenJobLogDO::getCreateTime)
        );
    }

    default int getScheduleTotalCount(Long jobId, LocalDateTime beginTime, LocalDateTime endTime){
        return selectCount(Wrappers.<OpenJobLogDO>lambdaQuery()
                .eq(Objects.nonNull(jobId), OpenJobLogDO::getJobId, jobId)
                .between(Objects.nonNull(beginTime), OpenJobLogDO::getCreateTime, beginTime, endTime)
        );
    }

    default int getScheduleSuccessTotalCount(Long jobId, LocalDateTime beginTime, LocalDateTime endTime){
        return selectCount(Wrappers.<OpenJobLogDO>lambdaQuery()
                .eq(Objects.nonNull(jobId), OpenJobLogDO::getJobId, jobId)
                .eq(OpenJobLogDO::getStatus, CommonStatusEnum.YES.getValue())
                .between(Objects.nonNull(beginTime), OpenJobLogDO::getCreateTime, beginTime, endTime)
        );
    }

    default void clearLog(LocalDateTime now, Integer interval){
        delete(Wrappers.<OpenJobLogDO>lambdaQuery()
                .lt(OpenJobLogDO::getCreateTime, now.plusDays(-interval))
        );
    }


    default List<OpenJobLogDO> getListByJobIdAndCount(Long jobId, Integer count){
        return selectList(Wrappers.<OpenJobLogDO>lambdaQuery()
                .eq(OpenJobLogDO::getJobId, jobId)
                .orderByDesc(OpenJobLogDO::getId)
                .last("limit " + count)
        );
    }
}
