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
package com.openbytecode.job.sample.components;

import com.openbytecode.rpc.server.hook.ShutdownHook;
import com.openbytecode.starter.executor.TaskExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author: 李俊平
 * @Date: 2023-09-17 09:50
 */
@Slf4j
@Component
public class JobShutdownHook implements ShutdownHook {

    private final TaskExecutor taskExecutor;

    public JobShutdownHook(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Override
    public void beforeShutdown() {
        killJobInQueue();
    }

    /**
     * 当容器销毁之前会等待任务线程执行完再销毁
     */
    private void killJobInQueue(){
        log.info("开始执行服务销毁之前的回调");
        taskExecutor.shutdown();
    }

}
