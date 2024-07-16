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
import com.openbytecode.job.admin.entity.OpenJobDO;
import com.openbytecode.job.api.dto.req.OpenJobReqDTO;
import com.openbytecode.job.common.enums.CommonStatusEnum;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

/**
 * 任务表
 *
 * @author lijunping
 * @email lijunping365@gmail.com
 * @date 2021-09-06 10:10:03
 */
@Repository
public interface OpenJobMapper extends BaseMapper<OpenJobDO> {

    default Page<OpenJobDO> queryPage(OpenJobReqDTO openJobReqDTO){
        return selectPage(openJobReqDTO.page(), Wrappers.<OpenJobDO>lambdaQuery()
                .like(Objects.nonNull(openJobReqDTO.getJobName()), OpenJobDO::getJobName, openJobReqDTO.getJobName())
                .eq(Objects.nonNull(openJobReqDTO.getStatus()), OpenJobDO::getStatus, openJobReqDTO.getStatus())
                .orderByDesc(OpenJobDO::getStatus)
                .orderByDesc(OpenJobDO::getCreateTime)
        );
    }

    default int getTotalCount(){
        return selectCount(Wrappers.<OpenJobDO>lambdaQuery());
    }

    default int getRunningCount(){
        return selectCount(Wrappers.<OpenJobDO>lambdaQuery()
                .eq(OpenJobDO::getStatus, CommonStatusEnum.YES.getValue())
        );
    }

    default List<OpenJobDO> queryStartJob(){
        return selectList(Wrappers.<OpenJobDO>lambdaQuery()
                .eq(OpenJobDO::getStatus, CommonStatusEnum.YES.getValue())
        );
    }

    default List<OpenJobDO> fetchByJobName(String jobName){
        return selectList(Wrappers.<OpenJobDO>lambdaQuery()
            .like(OpenJobDO::getJobName, jobName)
        );
    }

    default List<OpenJobDO> selectAll(){
        return selectList(null);
    }
}
