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
import com.openbytecode.job.admin.convert.OpenJobUserConvert;
import com.openbytecode.job.admin.entity.OpenJobUserDO;
import com.openbytecode.job.admin.mapper.OpenJobUserMapper;
import com.openbytecode.job.admin.service.OpenJobUserService;
import com.openbytecode.job.common.vo.PageResult;
import com.openbytecode.job.api.dto.batch.BatchDTO;
import com.openbytecode.job.api.dto.req.OpenJobUserReqDTO;
import com.openbytecode.starter.oauth.domain.UserDetails;
import com.openbytecode.starter.oauth.service.UserDetailService;
import com.openbytecode.job.api.dto.create.OpenJobUserCreateDTO;
import com.openbytecode.job.api.dto.resp.OpenJobUserRespDTO;
import com.openbytecode.job.api.dto.update.OpenJobUserUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
public class OpenJobUserServiceImpl extends ServiceImpl<OpenJobUserMapper, OpenJobUserDO> implements OpenJobUserService, UserDetailService {

    @Autowired
    private OpenJobUserMapper openJobUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public PageResult<OpenJobUserRespDTO> selectPage(OpenJobUserReqDTO openJobUserReqDTO) {
        Page<OpenJobUserDO> page = openJobUserMapper.queryPage(openJobUserReqDTO);
        IPage<OpenJobUserRespDTO> convert = page.convert(OpenJobUserConvert.INSTANCE::convert);
        return PageResult.build(convert);
    }

    @Override
    public OpenJobUserRespDTO getById(Long id) {
        OpenJobUserDO openJobUserDO = openJobUserMapper.selectById(id);
        return OpenJobUserConvert.INSTANCE.convert(openJobUserDO);
    }

    @Override
    public void save(OpenJobUserCreateDTO openJobUserCreateDTO) {
        passwordEncoder.encode(openJobUserCreateDTO.getPassword());
        openJobUserMapper.insert(OpenJobUserConvert.INSTANCE.convert(openJobUserCreateDTO));
    }

    @Override
    public void updateById(OpenJobUserUpdateDTO openJobUserUpdateDTO) {
        passwordEncoder.encode(openJobUserUpdateDTO.getPassword());
        openJobUserMapper.updateById(OpenJobUserConvert.INSTANCE.convert(openJobUserUpdateDTO));
    }

    @Override
    public void deleteBatchIds(BatchDTO batchDTO) {
        openJobUserMapper.deleteBatchIds(batchDTO.getIds());
    }

    @Override
    public OpenJobUserRespDTO loadUserByUserId(Long userId) {
        OpenJobUserDO openJobUserDO = this.openJobUserMapper.selectById(userId);
        return OpenJobUserConvert.INSTANCE.convert(openJobUserDO);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        OpenJobUserDO openJobUserDO = this.openJobUserMapper.loadUserByUsername(username);
        return convert(openJobUserDO);
    }

    @Override
    public UserDetails loadUserByMobile(String mobile) {
        OpenJobUserDO openJobUserDO = this.openJobUserMapper.loadUserByMobile(mobile);
        return convert(openJobUserDO);
    }

    private UserDetails convert(OpenJobUserDO openJobUserDO){
        if (Objects.isNull(openJobUserDO)){
            return null;
        }
        UserDetails userDetails = new UserDetails();
        userDetails.setId(openJobUserDO.getId());
        userDetails.setUsername(openJobUserDO.getUsername());
        userDetails.setPassword(openJobUserDO.getPassword());
        userDetails.setMobile(openJobUserDO.getPhone());
        userDetails.setAccountLocked(openJobUserDO.getStatus() == 1);
        return userDetails;
    }
}