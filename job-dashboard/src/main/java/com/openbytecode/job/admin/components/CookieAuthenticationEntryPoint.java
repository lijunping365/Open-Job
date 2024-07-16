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
package com.openbytecode.job.admin.components;

import com.openbytecode.starter.security.authentication.AuthenticationEntryPoint;
import com.openbytecode.starter.security.authentication.BearerTokenAuthenticationEntryPoint;
import com.openbytecode.starter.security.context.UserSecurityContext;
import com.openbytecode.starter.security.context.UserSecurityContextHolder;
import com.openbytecode.starter.security.domain.Authentication;
import com.openbytecode.starter.security.exception.InvalidBearerTokenException;
import com.openbytecode.starter.security.properties.SecurityProperties;
import com.openbytecode.starter.security.service.TokenService;
import com.openbytecode.starter.security.utils.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * <b>
 *  用户认证端口，不在白名单 {@link SecurityProperties#getIgnorePaths()} 内的请求都会经过此拦截器，认证失败
 *  会抛出 {@link InvalidBearerTokenException} 异常
 *  更多文档请参考
 *  @see <a href="https://www.openbytecode.com/starter/open-starter-security/docs/quick-start"> Open-Starter-Security Docs </a>
 *
 *  默认实现为 {@link BearerTokenAuthenticationEntryPoint} 其主要判断请求头中是否携带 Bearer token，
 *  并验证 Bearer token 的有效性
 *
 *  CookieAuthenticationEntryPoint 是自定义的认证端口实现，其主要判断请求 {@link HttpServletRequest}的 cookie 中是否有 accessToken
 *  并验证 Bearer token 的有效性
 * </b>
 *
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/6/27 20:34
 */
@Slf4j
@Component
public class CookieAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final TokenService tokenService;

    public CookieAuthenticationEntryPoint(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void commence(HttpServletRequest request) throws SecurityException {

        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            throw new InvalidBearerTokenException("no accessToken cookie found");
        }

        String accessToken = Arrays.stream(cookies)
                .filter(c -> c.getName().equalsIgnoreCase("accessToken"))
                .findAny()
                .orElseThrow(() -> new InvalidBearerTokenException("no accessToken cookie found"))
                .getValue();

        Authentication authentication = this.tokenService.readAuthentication(accessToken);
        UserSecurityContext user = JSON.parse(authentication.getUserDetails(), UserSecurityContext.class);
        UserSecurityContextHolder.setContext(user);
    }
}
