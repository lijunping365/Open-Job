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

import com.openbytecode.job.admin.components.ClusterInvoker;
import com.openbytecode.job.admin.entity.OpenJobAppDO;
import com.openbytecode.job.admin.entity.OpenJobDO;
import com.openbytecode.job.admin.entity.OpenJobLogDO;
import com.openbytecode.job.admin.event.JobLogEventPublisher;
import com.openbytecode.job.admin.mapper.OpenJobAppMapper;
import com.openbytecode.job.admin.mapper.OpenJobMapper;
import com.openbytecode.job.admin.service.OpenJobClientService;
import com.openbytecode.job.admin.service.OpenJobInstanceService;
import com.openbytecode.job.common.constants.CommonConstant;
import com.openbytecode.job.common.domain.MessageBody;
import com.openbytecode.job.common.domain.ResponseBody;
import com.openbytecode.job.common.enums.CommandEnum;
import com.openbytecode.job.common.enums.CommonStatusEnum;
import com.openbytecode.job.common.exception.ServiceException;
import com.openbytecode.job.common.json.JSON;
import com.openbytecode.job.common.log.JobLogResult;
import com.openbytecode.job.common.serialize.SerializationUtils;
import com.openbytecode.job.common.time.LocalDateTimeUtil;
import com.openbytecode.job.common.vo.ResultEnum;
import com.openbytecode.rpc.client.callback.CallCallback;
import com.openbytecode.rpc.client.remoting.RemotingInvoker;
import com.openbytecode.rpc.core.Message;
import com.openbytecode.rpc.core.enums.ResponseStatus;
import com.openbytecode.rpc.core.exception.NotFoundServerException;
import com.openbytecode.rpc.core.exception.RpcException;
import com.openbytecode.rpc.core.information.ServerInformation;
import com.openbytecode.rpc.core.transport.MessageResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lijunping on 2022/8/18
 */
@Slf4j
@Component
public class OpenJobClientServiceImpl implements OpenJobClientService {
    private final OpenJobMapper openJobMapper;
    private final ClusterInvoker clusterInvoker;
    private final RemotingInvoker remotingInvoker;
    private final OpenJobAppMapper openJobAppMapper;
    private final JobLogEventPublisher logEventPublisher;
    protected final OpenJobInstanceService instanceService;

    public OpenJobClientServiceImpl(OpenJobMapper openJobMapper,
                                    ClusterInvoker clusterInvoker,
                                    RemotingInvoker remotingInvoker,
                                    OpenJobAppMapper openJobAppMapper,
                                    JobLogEventPublisher logEventPublisher,
                                    OpenJobInstanceService instanceService) {
        this.openJobMapper = openJobMapper;
        this.clusterInvoker = clusterInvoker;
        this.remotingInvoker = remotingInvoker;
        this.openJobAppMapper = openJobAppMapper;
        this.logEventPublisher = logEventPublisher;
        this.instanceService = instanceService;
    }

    @Override
    public void invoke(Long jobId) {
        long startTime = System.currentTimeMillis();
        OpenJobDO jobDO = openJobMapper.selectById(jobId);
        LocalDateTime time = Objects.isNull(jobDO.getUpdateTime()) ? jobDO.getCreateTime(): jobDO.getUpdateTime();
        MessageBody messageBody = new MessageBody();
        messageBody.setAppId(jobDO.getAppId());
        messageBody.setJobId(jobDO.getId());
        messageBody.setHandlerName(jobDO.getHandlerName());
        messageBody.setExecutorTimeout(jobDO.getExecutorTimeout());
        messageBody.setParams(jobDO.getParams());
        messageBody.setScript(jobDO.getScript());
        messageBody.setScriptLang(jobDO.getScriptLang());
        messageBody.setScriptUpdateTime(String.valueOf(time.toEpochSecond(ZoneOffset.of("+8"))));
        messageBody.setCommand(CommandEnum.SCHEDULE.getValue());
        messageBody.setStartTime(startTime);

        byte[] serializeData = SerializationUtils.serialize(messageBody);
        OpenJobAppDO openJobAppDO = openJobAppMapper.selectById(jobDO.getAppId());
        Message message = new Message();
        message.setMsgId(String.valueOf(jobId));
        message.setBody(serializeData);
        message.setNamespace(openJobAppDO.getAppName());

        if (Objects.equals(jobDO.getSharding(), CommonStatusEnum.YES.getValue())){
            broadcastMessage(message, null);
            return;
        }

        try {
            clusterInvoker.invokeAsync(message, null);
        } catch (RpcException ex){
            logEventPublisher.saveOrUpdateLog(message, ex);
        }
    }

    @Override
    public void killTask(Long jobId, String serverId) {
        if (StringUtils.isBlank(serverId)){
            throw new ServiceException(ResultEnum.REMOTE_ID_NOT_EXISTED);
        }

        ServerInformation serverInformation = getServerInformation(serverId);
        MessageBody messageBody = new MessageBody();
        messageBody.setJobId(jobId);
        messageBody.setCommand(CommandEnum.KILL.getValue());
        byte[] serializeData = SerializationUtils.serialize(messageBody);
        Message message = new Message();
        message.setBody(serializeData);

        MessageResponseBody response;
        try {
            response = remotingInvoker.invoke(message, serverInformation);
        } catch (RpcException ex) {
            throw new ServiceException(ex.getMessage());
        }

        if (response.getStatus() != ResponseStatus.SUCCESS) {
            throw new ServiceException(response.getMsg());
        }
    }

    @Override
    public JobLogResult catLog(OpenJobLogDO openJobLogDO, Integer fromLineNum) {
        Long jobId = openJobLogDO.getJobId();
        String serverId = openJobLogDO.getServerId();
        if (StringUtils.isBlank(serverId)){
            throw new ServiceException(ResultEnum.REMOTE_ID_NOT_EXISTED);
        }

        ServerInformation serverInformation = getServerInformation(serverId);
        Message message = new Message();
        MessageBody messageBody = new MessageBody();
        messageBody.setJobId(jobId);
        messageBody.setLogId(openJobLogDO.getId());
        messageBody.setStartTime(LocalDateTimeUtil.getMillis(openJobLogDO.getStartTime()));
        messageBody.setCommand(CommandEnum.CAT_LOG.getValue());
        messageBody.setFromLineNum(fromLineNum);
        byte[] serializeData = SerializationUtils.serialize(messageBody);
        message.setBody(serializeData);

        MessageResponseBody response = null;
        try {
            response = remotingInvoker.invoke(message, serverInformation);
        } catch (RpcException ex) {
            throw new ServiceException(ex.getMessage());
        }

        if (response.getStatus() != ResponseStatus.SUCCESS) {
            throw new ServiceException(response.getMsg());
        }

        byte[] body = response.getBody();
        ResponseBody responseBody = SerializationUtils.deserialize(body, ResponseBody.class);
        return JSON.parse(responseBody.getData(), JobLogResult.class);
    }

    private ServerInformation getServerInformation(String serverId){
        String[] split = StringUtils.split(serverId, CommonConstant.Symbol.MH);
        return new ServerInformation(split[0], Integer.parseInt(split[1]));
    }

    protected void broadcastMessage(Message message, CallCallback callback) throws RpcException {
        List<ServerInformation> servers = instanceService.lookup(message.getNamespace());
        if (CollectionUtils.isEmpty(servers)) {
            throw new NotFoundServerException("No healthy server were found.");
        }

        List<String> serverIdList = servers.stream().map(ServerInformation::getServerId).collect(Collectors.toList());

        byte[] body = message.getBody();
        MessageBody messageBody = SerializationUtils.deserialize(body, MessageBody.class);
        messageBody.setShardingNodes(serverIdList);

        byte[] serialize = SerializationUtils.serialize(messageBody);
        message.setBody(serialize);

        for (ServerInformation server : servers) {
            try {
                remotingInvoker.invokeAsync(message, server, callback);
            } catch (RpcException ex) {
                logEventPublisher.saveOrUpdateLog(message, ex);
            }
        }
    }
}