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
package com.openbytecode.job.common.log;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: 李俊平
 * @Date: 2023-07-29 11:17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobLogResult implements Serializable {

    private static final long serialVersionUID = 5310302256501316302L;

    private int fromLineNum;
    private int toLineNum;
    private String logContent;
    private boolean isEnd;
}
