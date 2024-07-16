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
package com.openbytecode.job.admin.config;

import com.openbytecode.rpc.client.intercept.RequestInterceptor;
import com.openbytecode.rpc.client.intercept.ResponseInterceptor;
import com.openbytecode.rpc.client.random.RequestIdGenerator;
import com.openbytecode.rpc.client.remoting.NettyClient;
import com.openbytecode.rpc.client.remoting.NettyRemotingInvoker;
import com.openbytecode.rpc.client.remoting.RemotingClient;
import com.openbytecode.rpc.client.remoting.RemotingInvoker;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author lijunping on 2021/6/22
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Import({JacksonCustomizerConfiguration.class, CorsConfig.class})
public class SpringWebMvcConfig {

    /**
     * Spring Web Mvc 的全局异常，全局返回结果处理
     */
    @Configuration(proxyBeanMethods = false)
    static class WebMvcGlobalHandlerConfiguration {

        //全局异常
        @Bean
        @ConditionalOnMissingBean(GlobalExceptionHandler.class)
        public GlobalExceptionHandler globalExceptionHandler() {
            return new GlobalExceptionHandler();
        }
    }

    @Bean
    public RemotingClient remotingClient(){
        return new NettyClient();
    }

    @Bean
    public RemotingInvoker remotingInvoker(RemotingClient remotingClient,
                                           RequestIdGenerator requestIdGenerator,
                                           RequestInterceptor requestInterceptor,
                                           ResponseInterceptor responseInterceptor){
        return new NettyRemotingInvoker(remotingClient, requestIdGenerator, requestInterceptor, responseInterceptor);
    }
}
