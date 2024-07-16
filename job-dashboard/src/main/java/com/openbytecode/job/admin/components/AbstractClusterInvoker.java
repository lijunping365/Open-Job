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
import com.openbytecode.rpc.core.exception.NotFoundServerException;
import com.openbytecode.rpc.core.exception.RpcException;
import com.openbytecode.rpc.core.information.ServerInformation;
import com.openbytecode.rpc.core.transport.MessageResponseBody;
import com.openbytecode.rpc.loadbalance.LoadBalance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author lijunping on 2022/1/21
 */
@Slf4j
public abstract class AbstractClusterInvoker implements ClusterInvoker{

    protected final ClientConfiguration configuration;
    protected final LoadBalance loadBalance;
    protected final RemotingInvoker remotingInvoker;
    protected final OpenJobInstanceService instanceService;

    public AbstractClusterInvoker(ClientConfiguration configuration, LoadBalance loadBalance, RemotingInvoker remotingInvoker, OpenJobInstanceService instanceService){
        this.configuration = configuration;
        this.loadBalance = loadBalance;
        this.remotingInvoker = remotingInvoker;
        this.instanceService = instanceService;
    }

    @Override
    public MessageResponseBody invoke(Message messages) throws RpcException {
        final String namespace = messages.getNamespace();
        final List<ServerInformation> serverList = lookup(namespace);
        return doInvoke(messages, serverList);
    }

    @Override
    public void invokeAsync(Message messages, CallCallback callback) throws RpcException {
        final String namespace = messages.getNamespace();
        final List<ServerInformation> serverList = lookup(namespace);
        doInvokeAsync(messages, serverList, callback);
    }

    protected List<ServerInformation> lookup(String namespace) {
        List<ServerInformation> servers = instanceService.lookup(namespace);
        if (CollectionUtils.isEmpty(servers)) {
            throw new NotFoundServerException("No healthy server were found.");
        }
        return servers;
    }

    protected ServerInformation select(Message message, List<ServerInformation> servers) throws RpcException{
        return loadBalance.select(message, servers);
    }

    protected abstract MessageResponseBody doInvoke(Message message, List<ServerInformation> servers) throws RpcException;

    protected abstract void doInvokeAsync(Message message, List<ServerInformation> servers, CallCallback callback) throws RpcException;
}
