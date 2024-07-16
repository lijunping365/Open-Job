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
package com.openbytecode.job.common.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Objects;

/**
 * 返回的统一结果
 *
 * @author lijunping
 */
@EqualsAndHashCode
@ToString
@Getter
public class Result<T> implements Serializable {
  private static final long serialVersionUID = 2348142599948416257L;

  private Integer code;

  private String msg;

  private T data;

  protected Result() {
  }

  private Result(Integer code, String msg, T data) {
    this.code = code;
    this.msg = msg;
    this.data = data;
  }

  public static <T> Result<T> succeed() {
    return succeed(null, ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg());
  }

  public static <T> Result<T> succeed(T data) {
    return succeed(data, ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg());
  }

  public static <T> Result<T> succeed(T data, String msg) {
    return succeed(data, ResultEnum.SUCCESS.getCode(), msg);
  }

  public static <T> Result<T> succeed(Integer code, String msg) {
    return succeed(null, code, msg);
  }

  public static <T> Result<T> succeed(T data, Integer code, String msg) {
    return new Result<>(code, msg, data);
  }

  public static <T> Result<T> failed() {
    return failed(null, ResultEnum.BUSINESS_EXCEPTION.getCode(), ResultEnum.BUSINESS_EXCEPTION.getMsg());
  }

  public static <T> Result<T> failed(String msg) {
    return failed(null, ResultEnum.BUSINESS_EXCEPTION.getCode(), msg);
  }

  public static <T> Result<T> failed(T data, String msg) {
    return failed(data, ResultEnum.BUSINESS_EXCEPTION.getCode(), msg);
  }

  public static <T> Result<T> failed(Integer code, String msg) {
    return failed(null, code, msg);
  }

  public static <T> Result<T> failed(T data, Integer code, String msg) {
    return new Result<>(code, msg, data);
  }

  public boolean isSuccess() {
    return Objects.equals(ResultEnum.SUCCESS.getCode(), code);
  }

}
