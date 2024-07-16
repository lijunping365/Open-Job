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
import com.openbytecode.rpc.core.exception.FailbackException;
import com.openbytecode.rpc.core.exception.RpcException;
import com.openbytecode.rpc.core.information.ServerInformation;
import com.openbytecode.rpc.core.transport.MessageResponseBody;
import com.openbytecode.rpc.loadbalance.LoadBalance;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 失败重试调用模式
 *
 * @author lijunping 2022-01-31 19:29
 */
@Slf4j
public class FailbackClusterInvoker extends AbstractClusterInvoker {

    public FailbackClusterInvoker(ClientConfiguration configuration, LoadBalance loadBalance, RemotingInvoker remotingInvoker, OpenJobInstanceService instanceService) {
        super(configuration, loadBalance, remotingInvoker, instanceService);
    }

    @Override
    protected MessageResponseBody doInvoke(Message message, List<ServerInformation> servers) throws RpcException {
        ServerInformation serverInformation = super.select(message, servers);
        boolean success = false;
        int maxTimes = configuration.getRetryTimes();
        int currentTimes = 0;
        MessageResponseBody response = null;
        while (!success) {
            try {
                response = remotingInvoker.invoke(message, serverInformation);
                success = true;
            }catch (RpcException e){
                log.error(e.getMessage(), e);
            }
            if (!success) {
                currentTimes++;
                if (currentTimes > maxTimes) {
                    throw new FailbackException(serverInformation.getServerId(),
                            "The number of invoke retries reaches the upper limit, " +
                            "the maximum number of times：" + maxTimes);
                }
                try {
                    Thread.sleep(configuration.getRetryIntervalMilliSeconds());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return response;
    }

    @Override
    protected void doInvokeAsync(Message message, List<ServerInformation> servers, CallCallback callback) throws RpcException {
        ServerInformation serverInformation = super.select(message, servers);
        boolean success = false;
        int maxTimes = configuration.getRetryTimes();
        int currentTimes = 0;
        while (!success) {
            try {
                remotingInvoker.invokeAsync(message, serverInformation, callback);
                success = true;
            }catch (RpcException e){
                log.error(e.getMessage(), e);
            }
            if (!success) {
                currentTimes++;
                if (currentTimes > maxTimes) {
                    throw new FailbackException(serverInformation.getServerId(),
                            "The number of invoke retries reaches the upper limit, " +
                                    "the maximum number of times：" + maxTimes);
                }
                try {
                    Thread.sleep(configuration.getRetryIntervalMilliSeconds());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
