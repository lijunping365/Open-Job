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
package com.openbytecode.job.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author: 李俊平
 * @Date: 2024-01-20 13:12
 */
@Getter
@AllArgsConstructor
public enum ScriptTypeEnum {

    SCRIPT_SHELL("Shell Script", "bash", ".sh"),
    SCRIPT_PYTHON("Python Script", "python", ".py"),
    SCRIPT_PHP("PHP Script", "php", ".php"),
    SCRIPT_NODEJS("Nodejs Script", "node", ".js"),
    SCRIPT_POWERSHELL("PowerShell Script", "powershell", ".ps1");

    private final String desc;
    private final String cmd;
    private final String suffix;

    public static ScriptTypeEnum of(String cmd){
        for (ScriptTypeEnum typeEnum : ScriptTypeEnum.values()) {
            if (StringUtils.equals(typeEnum.getCmd(), cmd)){
                return typeEnum;
            }
        }
        return null;
    }

}
