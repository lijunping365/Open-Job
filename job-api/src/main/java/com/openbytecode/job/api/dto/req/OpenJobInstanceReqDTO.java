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
package com.openbytecode.job.api.dto.req;

import com.openbytecode.job.common.vo.PageQuery;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: 李俊平
 * @Date: 2022-02-26 15:03
 */
@Data
public class OpenJobInstanceReqDTO extends PageQuery implements Serializable {
    private static final long serialVersionUID = 4199379444084998224L;

    /**
     * 应用 id
     */
    private Long appId;
    /**
     * ip 地址
     */
    private String address;
    /**
     * 在线状态
     */
    private String status;
}
