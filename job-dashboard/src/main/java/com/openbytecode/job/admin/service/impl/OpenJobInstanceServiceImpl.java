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
import com.openbytecode.job.admin.convert.OpenJobInstanceConvert;
import com.openbytecode.job.admin.entity.OpenJobAppDO;
import com.openbytecode.job.admin.entity.OpenJobInstanceDO;
import com.openbytecode.job.admin.mapper.OpenJobAppMapper;
import com.openbytecode.job.admin.mapper.OpenJobInstanceMapper;
import com.openbytecode.job.admin.service.OpenJobInstanceService;
import com.openbytecode.job.common.domain.MessageBody;
import com.openbytecode.job.common.domain.ResponseBody;
import com.openbytecode.job.common.enums.CommandEnum;
import com.openbytecode.job.common.enums.CommonStatusEnum;
import com.openbytecode.job.common.exception.ServiceException;
import com.openbytecode.job.common.json.JSON;
import com.openbytecode.job.common.metrics.SystemMetricsInfo;
import com.openbytecode.job.common.serialize.SerializationUtils;
import com.openbytecode.job.common.time.LocalDateTimeUtil;
import com.openbytecode.job.common.vo.PageResult;
import com.openbytecode.job.common.vo.ResultEnum;
import com.openbytecode.job.api.dto.batch.BatchDTO;
import com.openbytecode.job.api.dto.create.OpenInstanceCreateDTO;
import com.openbytecode.job.api.dto.req.OpenJobInstanceReqDTO;
import com.openbytecode.job.api.dto.resp.OpenJobInstanceRespDTO;
import com.openbytecode.job.api.dto.update.OpenJobInstanceUpdateDTO;
import com.openbytecode.rpc.client.remoting.RemotingInvoker;
import com.openbytecode.rpc.core.Message;
import com.openbytecode.rpc.core.constants.CommonConstant;
import com.openbytecode.rpc.core.enums.ResponseStatus;
import com.openbytecode.rpc.core.enums.Status;
import com.openbytecode.rpc.core.exception.RpcException;
import com.openbytecode.rpc.core.information.ServerInformation;
import com.openbytecode.rpc.core.transport.MessageResponseBody;
import com.openbytecode.starter.security.context.UserSecurityContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author: 李俊平
 * @Date: 2022-02-26 15:06
 */
@Slf4j
@Service
public class OpenJobInstanceServiceImpl extends ServiceImpl<OpenJobInstanceMapper, OpenJobInstanceDO>  implements OpenJobInstanceService {

    /**
     *  12.3%(4 cores)
     */
    private static final String CPU_FORMAT = "%s / %s cores";
    /**
     *  27.7%(2.9/8.0 GB)
     */
    private static final String MEMORY_FORMAT = "%s%%（%s / %s GB）";

    private static final DecimalFormat df = new DecimalFormat("#.#");
    private final RemotingInvoker remotingInvoker;
    private final OpenJobAppMapper openJobAppMapper;
    private final OpenJobInstanceMapper openJobInstanceMapper;

    public OpenJobInstanceServiceImpl(RemotingInvoker remotingInvoker,
                                      OpenJobAppMapper openJobAppMapper,
                                      OpenJobInstanceMapper openJobInstanceMapper) {
        this.openJobAppMapper = openJobAppMapper;
        this.openJobInstanceMapper = openJobInstanceMapper;
        this.remotingInvoker = remotingInvoker;
    }

    @Override
    public PageResult<OpenJobInstanceRespDTO> selectPage(OpenJobInstanceReqDTO instanceReqDTO) {
        Page<OpenJobInstanceDO> page = openJobInstanceMapper.queryPage(instanceReqDTO);
        IPage<OpenJobInstanceRespDTO> convert = page.convert((this::convert));
        return PageResult.build(convert);

    }

    @Override
    public OpenJobInstanceRespDTO getInstanceById(Long appId, String serverId) {
        String[] serverIdArray = serverId.split(CommonConstant.Symbol.MH);
        OpenJobInstanceDO instanceDO = openJobInstanceMapper.selectByAppIdAndServerId(appId, serverIdArray[0], Integer.parseInt(serverIdArray[1]));
        return convert(instanceDO);
    }


    @Override
    public List<OpenJobInstanceRespDTO> getInstanceList(Long appId) {
        List<OpenJobInstanceDO> instances = openJobInstanceMapper.queryList(appId);
        return instances.stream().map((this::convert)).collect(Collectors.toList());
    }

    @Override
    public List<ServerInformation> lookup(String namespace) {
        OpenJobAppDO openJobAppDO = openJobAppMapper.getByAppName(namespace);
        List<OpenJobInstanceDO> instances = openJobInstanceMapper.queryList(openJobAppDO.getId());
        if (CollectionUtils.isEmpty(instances)){
            return Collections.emptyList();
        }
        return convert(instances);
    }

    @Override
    public void updateServerStatus(OpenJobInstanceUpdateDTO updateDTO) {
        OpenJobInstanceDO instanceDO = OpenJobInstanceConvert.INSTANCE.convert(updateDTO);
        instanceDO.setUpdateTime(LocalDateTime.now());
        instanceDO.setCreateUser(UserSecurityContextHolder.getUserId());
        openJobInstanceMapper.updateById(instanceDO);
    }

    @Override
    public void deleteBatchIds(BatchDTO batchDTO) {
        List<Long> ids = batchDTO.getIds();
        openJobInstanceMapper.deleteBatchIds(ids);
    }

    @Override
    public void add(OpenInstanceCreateDTO createDTO) {
        OpenJobInstanceDO openJobInstanceDO = new OpenJobInstanceDO();
        openJobInstanceDO.setAppId(createDTO.getAppId());
        String[] serverIdArray = createDTO.getServerId().split(CommonConstant.Symbol.MH);
        openJobInstanceDO.setAddress(serverIdArray[0]);
        openJobInstanceDO.setPort(Integer.parseInt(serverIdArray[1]));
        LocalDateTime now = LocalDateTime.now();
        openJobInstanceDO.setCreateTime(now);
        openJobInstanceDO.setUpdateTime(now);
        openJobInstanceDO.setCreateUser(UserSecurityContextHolder.getUserId());
        try {
            openJobInstanceMapper.insert(openJobInstanceDO);
        }catch (Exception e){
            throw new ServiceException(ResultEnum.INSTANCE_EXIST);
        }
    }

    private OpenJobInstanceRespDTO convert(OpenJobInstanceDO instanceDO) {
        OpenJobInstanceRespDTO instance = new OpenJobInstanceRespDTO();
        instance.setId(instanceDO.getId());
        instance.setServerId(instanceDO.getAddress() + CommonConstant.Symbol.MH + instanceDO.getPort());
        instance.setOnlineTime(instanceDO.getUpdateTime());
        instance.setStatus(instanceDO.getStatus());
        instance.setWeight(instanceDO.getWeight());
        if (CommonStatusEnum.of(instanceDO.getStatus()) == CommonStatusEnum.YES){
            instance.setLiveTime(LocalDateTimeUtil.getTimeBetween(instanceDO.getUpdateTime(), LocalDateTime.now()));
        }
        doWrapper(instance);
        return instance;
    }

    private void doWrapper(OpenJobInstanceRespDTO instance){
        Message message = new Message();
        MessageBody messageBody = new MessageBody();
        messageBody.setCommand(CommandEnum.METRICS.getValue());
        message.setBody(SerializationUtils.serialize(messageBody));
        String[] serverIdArray = instance.getServerId().split(CommonConstant.Symbol.MH);
        ServerInformation serverInformation = new ServerInformation(serverIdArray[0], Integer.parseInt(serverIdArray[1]));

        MessageResponseBody messageResponseBody;
        try {
            messageResponseBody = doInvoke(message, serverInformation);
        }catch (RpcException ex){
            log.error(ex.getMessage(), ex);
            return;
        }

        byte[] body = messageResponseBody.getBody();
        ResponseBody response = SerializationUtils.deserialize(body, ResponseBody.class);
        if (StringUtils.isBlank(response.getData())){
            return;
        }

        SystemMetricsInfo metricsInfo = JSON.parse(response.getData(), SystemMetricsInfo.class);
        wrapperMetricsInfo(instance, metricsInfo);
    }

    private void wrapperMetricsInfo(OpenJobInstanceRespDTO instance, SystemMetricsInfo metricsInfo){
        if (Objects.isNull(metricsInfo)){
            return;
        }
        // CPU 指标
        String cpuInfo = String.format(CPU_FORMAT, df.format(metricsInfo.getCpuLoad()), metricsInfo.getCpuProcessors());
        // 内存指标
        String menU = df.format(metricsInfo.getJvmMemoryUsage() * 100);
        String menUsed = df.format(metricsInfo.getJvmUsedMemory());
        String menMax = df.format(metricsInfo.getJvmMaxMemory());
        String memoryInfo = String.format(MEMORY_FORMAT, menU, menUsed, menMax);
        // 磁盘指标
        String diskU = df.format(metricsInfo.getDiskUsage() * 100);
        String diskUsed = df.format(metricsInfo.getDiskUsed());
        String diskMax = df.format(metricsInfo.getDiskTotal());
        String diskInfo = String.format(MEMORY_FORMAT, diskU, diskUsed, diskMax);

        instance.setCpuInfo(cpuInfo);
        instance.setMemoryInfo(memoryInfo);
        instance.setDiskInfo(diskInfo);
    }

    private MessageResponseBody doInvoke(Message message, ServerInformation serverInformation){
        MessageResponseBody response;
        try {
            response = remotingInvoker.invoke(message, serverInformation);
        }catch (RpcException e){
            throw new RpcException(e.getMessage());
        }
        if (Objects.nonNull(response) && response.getStatus() != ResponseStatus.SUCCESS){
            throw new RpcException("处理失败");
        }
        return response;
    }

    private List<ServerInformation> convert(List<OpenJobInstanceDO> instances){
        if (CollectionUtils.isEmpty(instances)){
            return new ArrayList<>();
        }
        return instances.stream().map(instance -> {
            ServerInformation serverInfo = ServerInformation.valueOf(instance.getAddress(), instance.getPort());
            serverInfo.setStatus(Status.ON_LINE);
            return serverInfo;
        }).collect(Collectors.toList());
    }
}
