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
package com.openbytecode.job.sample.config;

import com.openbytecode.job.common.thread.NamedThreadFactory;
import com.openbytecode.rpc.server.ServerConfiguration;
import com.openbytecode.rpc.server.hook.ShutdownHook;
import com.openbytecode.rpc.server.process.MessageProcess;
import com.openbytecode.rpc.server.remoting.MessageHandler;
import com.openbytecode.rpc.server.remoting.NettyMessageHandler;
import com.openbytecode.rpc.server.remoting.NettyServer;
import com.openbytecode.rpc.server.remoting.RemotingServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: 李俊平
 * @Date: 2022-03-19 10:24
 */
@Configuration
public class OpenJobConfiguration {

    @Bean(destroyMethod = "shutdown")
    public ThreadPoolExecutor threadPoolExecutor(){
        return new ThreadPoolExecutor(
                0,
                200,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(2000),
                new NamedThreadFactory("Open-Job"),
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        throw new RuntimeException("JobThreadPool is EXHAUSTED!");
                    }
                });
    }

    @Bean
    public MessageHandler messageHandler(MessageProcess messageProcess){
        return new NettyMessageHandler(messageProcess);
    }

    @Bean
    public RemotingServer remotingServer(ShutdownHook shutdownHook,
                                         MessageHandler messageHandler,
                                         ServerConfiguration configuration){
        return new NettyServer(shutdownHook, messageHandler, configuration);
    }
}
