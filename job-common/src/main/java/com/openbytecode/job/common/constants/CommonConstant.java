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
package com.openbytecode.job.common.constants;

/**
 * @author 李俊平
 */
public interface CommonConstant {
  /**
   * 认证请求头
   */
  String AUTHENTICATION_HEADER = "Authorization";

  /**
   * Basic Token 前缀
   */
  String AUTHENTICATION_TYPE = "Bearer ";

  /**
   * 用户信息Http请求头
   */
  String USER_TOKEN_HEADER = "user";

  /**
   * 统一用户 id
   */
  String USER_ID = "id";

  /**
   * ip:port pattern
   */
  String ADDRESS_PATTERN = "%s:%d";


  public static final class Symbol {
    private Symbol() {
    }

    /**
     * The constant COMMA.
     */
    public static final String COMMA = ",";
    public static final String SPOT = ".";
    /**
     * The constant UNDER_LINE.
     */
    public final static String UNDER_LINE = "_";
    /**
     * The constant PER_CENT.
     */
    public final static String PER_CENT = "%";
    /**
     * The constant AT.
     */
    public final static String AT = "@";
    /**
     * The constant PIPE.
     */
    public final static String PIPE = "||";
    public final static String SHORT_LINE = "-";
    public final static String SPACE = " ";
    public static final String SLASH = "/";
    public static final String MH = ":";

  }

  /**
   * 默认的脚本执行处理器名称
   */
  String SCRIPT_HANDLER_NAME = "script-handler";

}
