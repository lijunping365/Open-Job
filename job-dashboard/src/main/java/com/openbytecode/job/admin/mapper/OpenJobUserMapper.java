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
import com.openbytecode.job.api.dto.req.OpenJobUserReqDTO;
import org.apache.commons.lang3.StringUtils;
import com.openbytecode.job.admin.entity.OpenJobUserDO;
import org.springframework.stereotype.Repository;

/**
 * 用户表
 * 
 * @author lijunping
 * @email lijunping365@gmail.com
 * @date 2021-06-22 15:20:30
 */
@Repository
public interface OpenJobUserMapper extends BaseMapper<OpenJobUserDO> {

    default OpenJobUserDO loadUserByUsername(String username){
        return selectOne(Wrappers.<OpenJobUserDO>lambdaQuery()
                .eq(OpenJobUserDO::getUsername, username)
                .last("limit 1")
        );
    }

    default OpenJobUserDO loadUserByMobile(String mobile){
        return selectOne(Wrappers.<OpenJobUserDO>lambdaQuery()
                .eq(OpenJobUserDO::getPhone, mobile)
                .last("limit 1")
        );
    }

    default Page<OpenJobUserDO> queryPage(OpenJobUserReqDTO openJobUserReqDTO){
        return selectPage(openJobUserReqDTO.page(), Wrappers.<OpenJobUserDO>lambdaQuery()
                .like(StringUtils.isNotBlank(openJobUserReqDTO.getUsername()), OpenJobUserDO::getUsername, openJobUserReqDTO.getUsername())
                .orderByDesc(OpenJobUserDO::getCreateTime)
        );
    }
}
