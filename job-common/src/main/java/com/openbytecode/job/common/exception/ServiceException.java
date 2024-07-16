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
package com.openbytecode.job.common.exception;

import com.openbytecode.job.common.vo.ResultEnum;

/**
 * 业务逻辑异常 Exception
 *
 * @author lijunping
 */
public class ServiceException extends BaseException {

  public ServiceException(String msg) {
    super(msg);
  }

  public ServiceException(ResultEnum resultEnum) {
    super(resultEnum.getCode(), resultEnum.getMsg());
  }

  public ServiceException(Integer code, String msg) {
    super(code, msg);
  }

  public ServiceException(Exception e) {
    super(e);
  }

}
