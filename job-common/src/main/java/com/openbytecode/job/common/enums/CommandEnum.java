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

import org.apache.commons.lang3.StringUtils;

/**
 * @author lijunping on 2023/5/12
 */
public enum CommandEnum {

    /**
     * kill task
     */
    KILL("kill"),

    /**
     * Query metrics
     */
    METRICS("metrics"),

    /**
     * do invoke
     */
    SCHEDULE("schedule"),

    /**
     * cat log
     */
    CAT_LOG("cat_log"),

    /**
     * query handler
     */
    QUERY_HANDLER("query_handler"),

    ;

    private final String value;

    CommandEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static CommandEnum of(String value){
        for (CommandEnum cacheCommand : values()) {
            if (StringUtils.equals(cacheCommand.getValue(), value)){
                return cacheCommand;
            }
        }
        return null;
    }
}
