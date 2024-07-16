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

import com.openbytecode.job.sample.components.JobHandlerParam;
import com.openbytecode.starter.job.register.annotation.JobHandler;
import com.openbytecode.starter.job.register.core.OpenJobHandler;
import com.openbytecode.starter.logger.Logger;
import com.openbytecode.starter.logger.LoggerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author lijunping on 2022/2/25
 */
@Slf4j
@JobHandler(value = "job-one", name = "job-one")
@Component
public class OpenJobHandlerOne implements OpenJobHandler<JobHandlerParam> {
    private static final Logger logger = LoggerFactory.getLogger();

    @Override
    public void handler(JobHandlerParam jobParam) throws Exception {
        log.info("JobHandlerOne 处理任务");
        logger.log("任务正在处理... {}", jobParam.getJobId());
        TimeUnit.SECONDS.sleep(3);
        logger.log("已处理 3 second... {}", jobParam.getJobId());
        TimeUnit.SECONDS.sleep(5);
        //throw new RuntimeException("test exception");
        logger.log("处理完成 completed... {}", jobParam.getJobId());
    }
}
