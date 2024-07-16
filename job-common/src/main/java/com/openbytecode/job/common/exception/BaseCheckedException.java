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

/**
 * 基类检查异常
 *
 * @author lijunping
 */
public class BaseCheckedException extends Exception {
  private static final long serialVersionUID = 3470726438554407056L;

  /**
   * 业务异常固定错误码，主要要来给用户提示某些信息
   */
  private Integer code = 1000;

  public BaseCheckedException(String message) {
    super(message);
  }

  public BaseCheckedException(Throwable cause) {
    super(cause.getMessage());
  }

  public BaseCheckedException(Integer code, String message) {
    this(message);
    this.code = code;
  }

  public Integer getCode() {
    return code;
  }

  /**
   * 重写此方法提升性能
   */
  @Override
  public Throwable fillInStackTrace() {
    return this;
  }
}
