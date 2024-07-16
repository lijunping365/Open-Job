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
package com.openbytecode.job.common.script;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 脚本
 */
@Slf4j
public class ScriptUtil {


    /**
     * 执行脚本
     *
     * @param command
     * @param scriptFile
     * @param params
     * @return
     * @throws IOException
     */
    public static int execToFile(String command, String scriptFile, List<String> params) throws IOException {
        try {
            List<String> cmdArray = new ArrayList<>();
            cmdArray.add(command);
            cmdArray.add(scriptFile);
            if (!CollectionUtils.isEmpty(params)) cmdArray.addAll(params);

            // process-exec
            final Process process = Runtime.getRuntime().exec(cmdArray.toArray(new String[0]));

            // process-wait
            return process.waitFor();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return -1;
        }
    }
}
