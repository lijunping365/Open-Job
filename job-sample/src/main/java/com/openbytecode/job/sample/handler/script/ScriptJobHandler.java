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
package com.openbytecode.job.sample.handler.script;

import com.openbytecode.job.common.constants.CommonConstant;
import com.openbytecode.job.common.enums.ScriptTypeEnum;
import com.openbytecode.job.common.file.FileUtils;
import com.openbytecode.job.common.script.ScriptUtil;
import com.openbytecode.job.sample.components.JobHandlerParam;
import com.openbytecode.starter.job.register.annotation.JobHandler;
import com.openbytecode.starter.job.register.core.OpenJobHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

/**
 * @author: 李俊平
 * @Date: 2023-03-01 10:03
 */
@Slf4j
@JobHandler(value = CommonConstant.SCRIPT_HANDLER_NAME, name = "脚本任务处理器")
@Component
public class ScriptJobHandler implements OpenJobHandler<JobHandlerParam> {

    private static String scriptPath = "/data/server/open-job/script";

    static {
        File logPathDir = new File(scriptPath);
        if (!logPathDir.exists()) {
            boolean mkdirs = logPathDir.mkdirs();
        }
        scriptPath = logPathDir.getPath();
    }

    @Override
    public void handler(JobHandlerParam jobParam) throws Exception {
        String script = jobParam.getScript();
        String scriptParams = jobParam.getParams();
        String scriptLang = jobParam.getScriptLang();
        String scriptUpdateTime = jobParam.getScriptUpdateTime();

        if (StringUtils.isBlank(script) || StringUtils.isBlank(scriptLang)){
            return;
        }

        ScriptTypeEnum scriptType = ScriptTypeEnum.of(scriptLang);

        String timestamp = StringUtils.isBlank(scriptUpdateTime) ?
                String.valueOf(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"))) :
                scriptUpdateTime;

        // make script file
        String scriptFileName = scriptPath
                .concat(File.separator)
                .concat(String.valueOf(jobParam.getJobId()))
                .concat("_")
                .concat(timestamp)
                .concat(scriptType.getSuffix());

        File scriptFile = new File(scriptFileName);
        if (!scriptFile.exists()) {
            FileUtils.writeToFile(scriptFileName, script);
        }



        log.info("script file: {}", scriptFileName);

        List<String> params = StringUtils.isBlank(scriptParams) ? null : Arrays.asList(scriptParams.split(","));

        // invoke
        int exitValue = ScriptUtil.execToFile(scriptType.getCmd(), scriptFileName, params);

        if (exitValue == 0) {
            log.info("执行成功");
        } else {
            log.error("script exit value("+exitValue+") is failed");
        }
    }
}
