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
import com.openbytecode.job.admin.entity.OpenJobInstanceDO;
import com.openbytecode.job.api.dto.req.OpenJobInstanceReqDTO;
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
public interface OpenJobInstanceMapper extends BaseMapper<OpenJobInstanceDO> {

    default Page<OpenJobInstanceDO> queryPage(OpenJobInstanceReqDTO instanceReqDTO){
        return selectPage(instanceReqDTO.page(), Wrappers.<OpenJobInstanceDO>lambdaQuery()
                .eq(Objects.nonNull(instanceReqDTO.getAppId()), OpenJobInstanceDO::getAppId, instanceReqDTO.getAppId())
                .like(Objects.nonNull(instanceReqDTO.getAddress()), OpenJobInstanceDO::getAddress, instanceReqDTO.getAddress())
                .eq(Objects.nonNull(instanceReqDTO.getStatus()), OpenJobInstanceDO::getStatus, instanceReqDTO.getStatus())
                .orderByDesc(OpenJobInstanceDO::getCreateTime)
        );
    }

    default List<OpenJobInstanceDO> queryList(Long appId){
        return selectList(Wrappers.<OpenJobInstanceDO>lambdaQuery()
                .eq(OpenJobInstanceDO::getAppId, appId)
                .eq(OpenJobInstanceDO::getStatus, CommonStatusEnum.YES.getValue())
                .orderByDesc(OpenJobInstanceDO::getCreateTime)
        );
    }

    default OpenJobInstanceDO selectByAppIdAndServerId(Long appId, String address, int port){
        return selectOne(Wrappers.<OpenJobInstanceDO>lambdaQuery()
                .eq(OpenJobInstanceDO::getAppId, appId)
                .eq(OpenJobInstanceDO::getAddress, address)
                .eq(OpenJobInstanceDO::getPort, port));
    }

    default List<OpenJobInstanceDO> getAllInstance(){
        return selectList(null);
    }

}
