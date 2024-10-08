/*
 * Copyright © 2022 Open-Light-Rpc Authors. All Rights Reserved.
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
package com.openbytecode.job.admin.components;


import com.openbytecode.job.admin.service.OpenJobInstanceService;
import com.openbytecode.rpc.client.ClientConfiguration;
import com.openbytecode.rpc.client.callback.CallCallback;
import com.openbytecode.rpc.client.remoting.RemotingInvoker;
import com.openbytecode.rpc.core.Message;
import com.openbytecode.rpc.core.exception.FailoverException;
import com.openbytecode.rpc.core.exception.RpcException;
import com.openbytecode.rpc.core.information.ServerInformation;
import com.openbytecode.rpc.core.transport.MessageResponseBody;
import com.openbytecode.rpc.loadbalance.LoadBalance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 故障转移模式
 *
 * @author lijunping 2022-02-02 08:40
 */
@Slf4j
@Component
public class FailoverClusterInvoker extends AbstractClusterInvoker {

    public FailoverClusterInvoker(ClientConfiguration configuration, LoadBalance loadBalance, RemotingInvoker remotingInvoker, OpenJobInstanceService instanceService) {
        super(configuration, loadBalance, remotingInvoker, instanceService);
    }

    @Override
    protected MessageResponseBody doInvoke(Message message, List<ServerInformation> servers) throws RpcException {
        ServerInformation serverInformation = super.select(message, servers);
        MessageResponseBody response;
        try {
            response = remotingInvoker.invoke(message, serverInformation);
        } catch (RpcException e){
            servers.remove(serverInformation);
            if (CollectionUtils.isEmpty(servers)){
                throw new FailoverException(serverInformation.getServerId(), e.getMessage());
            }
            response = invoke(message, servers);
        }
        return response;
    }

    @Override
    protected void doInvokeAsync(Message message, List<ServerInformation> servers, CallCallback callback) throws RpcException {
        ServerInformation serverInformation = super.select(message, servers);
        try {
            remotingInvoker.invokeAsync(message, serverInformation, callback);
        } catch (RpcException e){
            servers.remove(serverInformation);
            if (CollectionUtils.isEmpty(servers)){
                throw new FailoverException(serverInformation.getServerId(), e.getMessage());
            }
            invokeAsync(message, servers, callback);
        }
    }

    private MessageResponseBody invoke(Message message, List<ServerInformation> servers) throws RpcException{
        RpcException ex = null;
        MessageResponseBody response = null;
        for (ServerInformation serverInformation : servers) {
            try {
                response = remotingInvoker.invoke(message, serverInformation);
                break;
            }catch (RpcException e){
                ex = e;
            }
        }
        if (response == null && ex != null){
            throw new FailoverException(servers.get(servers.size() -1).getServerId(), ex.getMessage());
        }
        return response;
    }

    private void invokeAsync(Message message, List<ServerInformation> servers, CallCallback callback) throws RpcException{
        RpcException ex = null;
        for (ServerInformation serverInformation : servers) {
            try {
                remotingInvoker.invokeAsync(message, serverInformation, callback);
                return;
            }catch (RpcException e){
                ex = e;
            }
        }
        if (ex != null){
            throw new FailoverException(servers.get(servers.size() -1).getServerId(), ex.getMessage());
        }
    }
}
