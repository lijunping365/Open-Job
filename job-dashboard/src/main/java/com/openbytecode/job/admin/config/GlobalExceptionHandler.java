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
package com.openbytecode.job.admin.config;

import com.openbytecode.job.common.exception.BaseCheckedException;
import com.openbytecode.job.common.exception.BaseException;
import com.openbytecode.job.common.exception.ServiceException;
import com.openbytecode.job.common.vo.Result;
import com.openbytecode.job.common.vo.ResultEnum;
import com.openbytecode.starter.security.exception.AccessDeniedException;
import com.openbytecode.starter.security.exception.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 全局异常处理器，将 Exception 翻译成 CommonResult + 对应的异常编号
 *
 * @author lijunping
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * 处理 SpringMVC 请求方法不正确
   * <p>
   * 例如说，A 接口的方法为 GET 方式，结果请求方法为 POST 方式，导致不匹配
   */
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public Result<Object> httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException ex) {
    log.warn("[httpRequestMethodNotSupportedExceptionHandler]", ex);
    return Result.failed(HttpStatus.BAD_REQUEST.value(), String.format("请求方法不正确:%s", ex.getMessage()));
  }

  @ExceptionHandler({IllegalArgumentException.class})
  public Result<Object> badRequest(IllegalArgumentException ex) {
    log.warn("[illegalArgumentExceptionHandler]", ex);
    return Result.failed(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
  }

  /**
   * 处理 SpringMVC 404 异常
   */
  @ExceptionHandler({NoHandlerFoundException.class})
  public Result<Object> badRequest(NoHandlerFoundException ex) {
    log.warn("[noHandlerFoundExceptionHandler]", ex);
    return Result.failed(HttpStatus.NOT_FOUND.value(), ex.getMessage());
  }

  /**
   * 处理 SpringMVC 请求参数缺失
   * <p>
   * 例如说，接口上设置了 @RequestParam("xx") 参数，结果并未传递 xx 参数
   */
  @ExceptionHandler({MissingServletRequestParameterException.class})
  public Result<Object> handleError(MissingServletRequestParameterException ex) {
    log.warn("[missingServletRequestParameterExceptionHandler]", ex);
    return Result.failed(HttpStatus.BAD_REQUEST.value(), String.format("请求参数缺失:%s", ex.getParameterName()));
  }

  /**
   * 处理 SpringMVC 请求参数类型错误
   * <p>
   * 例如说，接口上设置了 @RequestParam("xx") 参数为 Integer，结果传递 xx 参数类型为 String
   */
  @ExceptionHandler({MethodArgumentTypeMismatchException.class})
  public Result<Object> handleError(MethodArgumentTypeMismatchException ex) {
    log.warn("[missingServletRequestParameterExceptionHandler]", ex);
    return Result.failed(HttpStatus.BAD_REQUEST.value(), String.format("请求参数类型错误:%s", ex.getMessage()));
  }

  /**
   * 处理 SpringMVC 参数校验不正确，使用 （Validator（@NotNull...） + 自动抛出异常） 可以省略 BindingResult 这一步
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Result<Object> methodArgumentNotValid(MethodArgumentNotValidException ex) {
    log.warn("[methodArgumentNotValidExceptionHandler]", ex);
    List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();

    Map<String, String> errors = allErrors.stream()
      .map(error -> Collections.singletonMap(((FieldError) error).getField(), error.getDefaultMessage()))
      .flatMap(stringStringMap -> stringStringMap.entrySet().stream())
      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    String errorMsg = allErrors.stream()
      .map(error -> ((FieldError) error).getField() + ":" + error.getDefaultMessage())
      .collect(Collectors.joining(";"));

    return Result.failed(errors, HttpStatus.BAD_REQUEST.value(), String.format("请求参数不正确:%s", errorMsg));
  }

  /**
   * 处理 SpringMVC 参数绑定不正确，本质上也是通过 Validator 校验
   */
  @ExceptionHandler(BindException.class)
  public Result<Object> bind(BindException ex) {
    log.warn("[bindExceptionHandler]", ex);
    List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

    Map<String, String> errors = fieldErrors.stream()
      .map(fieldError -> Collections.singletonMap(fieldError.getField(), fieldError.getDefaultMessage()))
      .flatMap(stringStringMap -> stringStringMap.entrySet().stream())
      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    String msg = fieldErrors.stream()
      .map(fieldError -> fieldError.getField() + ":" + fieldError.getDefaultMessage())
      .collect(Collectors.joining(";"));

    return Result.failed(errors, HttpStatus.BAD_REQUEST.value(), String.format("请求参数不正确:%s", msg));
  }

  /**
   * 处理业务异常 ServiceException
   * <p>
   * 例如说，商品库存不足，用户手机号已存在。
   */
  @ExceptionHandler({ServiceException.class})
  public Result<Object> serviceException(ServiceException ex) {
    log.warn("[serviceExceptionHandler]", ex);
    return Result.failed(ex.getCode(), ex.getMessage());
  }

  /**
   * 处理基础异常 BaseException
   */
  @ExceptionHandler({BaseException.class})
  public Result<Object> baseException(BaseException ex) {
    log.warn("[baseExceptionHandler]", ex);
    return Result.failed(ex.getCode(), ex.getMessage());
  }

  /**
   * 处理基础异常 BaseCheckedException
   */
  @ExceptionHandler({BaseCheckedException.class})
  public Result<Object> baseCheckedException(BaseCheckedException ex) {
    log.warn("[baseCheckedExceptionHandler]", ex);
    return Result.failed(ex.getCode(), ex.getMessage());
  }

  @ExceptionHandler({SecurityException.class})
  public Result<Object> securityException(SecurityException ex) {
    log.warn("[securityException]", ex);
    if (ex instanceof AccessDeniedException){
      return Result.failed(ResultEnum.FORBIDDEN.getMsg());
    }
    return Result.failed(ResultEnum.UNAUTHORIZED.getCode(), ResultEnum.UNAUTHORIZED.getMsg());
  }

  @ExceptionHandler({RuntimeException.class})
  public Result<Object> runtime(RuntimeException ex) {
    log.warn("[runtimeExceptionHandler]", ex);
    return Result.failed(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
  }

  @ExceptionHandler({Exception.class})
  public Result<Object> exception(HttpServletRequest request, Exception ex) {
    log.warn("[exceptionHandler]", ex);
    return Result.failed(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
  }

  @ExceptionHandler({Throwable.class})
  public Result<Object> error(HttpServletRequest request, Throwable ex) {
    log.warn("[throwableHandler]", ex);
    return Result.failed(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
  }

}
