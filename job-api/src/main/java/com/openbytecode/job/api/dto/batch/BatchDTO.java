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
package com.openbytecode.job.api.dto.batch;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * 分类表
 *
 * @author lijunping
 * @email lijunping365@gmail.com
 * @date 2021-06-22 15:35:38
 */
@Data
public class BatchDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "要操作的主键id集合不能为空")
    private List<Long> ids;

}
