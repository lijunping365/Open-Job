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
import com.openbytecode.job.admin.entity.OpenJobReportDO;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author lijunping on 2022/4/11
 */
@Repository
public interface OpenJobReportMapper extends BaseMapper<OpenJobReportDO> {

    default List<OpenJobReportDO> queryList(Long jobId, LocalDateTime beginTime, LocalDateTime endTime){
        return selectList(Wrappers.<OpenJobReportDO>lambdaQuery()
                .eq(Objects.nonNull(jobId), OpenJobReportDO::getJobId, jobId)
                .between(Objects.nonNull(beginTime), OpenJobReportDO::getCreateTime, beginTime, endTime)
                .orderByDesc(OpenJobReportDO::getCreateTime)
        );
    }
}
