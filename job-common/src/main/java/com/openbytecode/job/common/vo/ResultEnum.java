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

/**
 * 结果枚举
 *
 * @author lijunping
 */
public enum ResultEnum {

  SUCCESS(200, "成功"), UNAUTHORIZED(401, "认证失败"),

  BUSINESS_EXCEPTION("业务异常"), USERNAME_OR_PASSWORD_ERROR("用户名或密码错误"), FORBIDDEN("权限不足，无法访问"),

  TASK_COMPLETED("任务已完成，无法杀死"), REMOTE_ID_NOT_EXISTED("任务远程执行地址不存在"), INSTANCE_EXIST("节点已存在，请勿重复添加"),

  ;

  private final Integer code;

  private final String msg;

  ResultEnum(String msg) {
    this.code = 1000;
    this.msg = msg;
  }

  ResultEnum(Integer code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public Integer getCode() {
    return code;
  }

  public String getMsg() {
    return msg;
  }

}
