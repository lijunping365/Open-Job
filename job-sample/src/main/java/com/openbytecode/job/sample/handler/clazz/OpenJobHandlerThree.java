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
package com.openbytecode.job.sample.handler.clazz;

import com.openbytecode.starter.job.register.annotation.JobHandler;
import com.openbytecode.starter.job.register.core.OpenJobHandler;
import com.openbytecode.starter.job.register.param.HandlerParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author lijunping on 2022/2/25
 */
@Slf4j
@JobHandler(value = "job-three", name = "job-three")
@Component
public class OpenJobHandlerThree implements OpenJobHandler {

    @Override
    public void handler(HandlerParam jobParam) {
        log.info("JobHandlerThree 处理任务");
    }
}
