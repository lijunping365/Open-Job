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
import com.openbytecode.job.api.dto.req.OpenJobAppReqDTO;
import com.openbytecode.job.admin.entity.OpenJobAppDO;
import org.apache.commons.lang3.StringUtils;
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
public interface OpenJobAppMapper extends BaseMapper<OpenJobAppDO> {

    default Page<OpenJobAppDO> queryPage(OpenJobAppReqDTO openJobAppReqDTO){
        return selectPage(openJobAppReqDTO.page(), Wrappers.<OpenJobAppDO>lambdaQuery()
                .like(Objects.nonNull(openJobAppReqDTO.getAppName()), OpenJobAppDO::getAppName, openJobAppReqDTO.getAppName())
                .orderByDesc(OpenJobAppDO::getCreateTime)
        );
    }

    default List<OpenJobAppDO> queryList(String appName){
        return selectList(Wrappers.<OpenJobAppDO>lambdaQuery()
                .like(StringUtils.isNotBlank(appName), OpenJobAppDO::getAppName, appName)
        );
    }

    default OpenJobAppDO getByAppName(String namespace){
        return selectOne(Wrappers.<OpenJobAppDO>lambdaQuery()
                .eq(OpenJobAppDO::getAppName, namespace)
                .last("limit 1")
        );
    }
}
