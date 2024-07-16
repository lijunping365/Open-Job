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
package com.openbytecode.job;

import com.openbytecode.rpc.client.annotation.EnableOpenRpcClient;
import com.openbytecode.starter.openai.annotation.EnableOpenAI;
import com.openbytecode.starter.schedule.annotation.EnableOpenScheduler;
import com.openbytecode.starter.security.annotation.EnableSecurity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author lijunping on 2022/2/24
 */
@EnableOpenAI
@EnableSecurity
@EnableScheduling
@EnableOpenScheduler
@EnableOpenRpcClient
@SpringBootApplication
public class JobDashboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobDashboardApplication.class, args);
    }
}
