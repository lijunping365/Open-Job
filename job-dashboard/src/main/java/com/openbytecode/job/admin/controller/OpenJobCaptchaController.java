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
package com.openbytecode.job.admin.controller;

import com.openbytecode.job.api.dto.req.OpenJobCaptchaRequest;
import com.openbytecode.job.api.dto.resp.OpenJobCaptchaRespDTO;
import com.openbytecode.job.common.vo.Result;
import com.openbytecode.starter.captcha.core.image.ImageCodeGenerator;
import com.openbytecode.starter.captcha.core.image.ImageValidateCode;
import com.openbytecode.starter.captcha.core.sms.SmsCodeGenerator;
import com.openbytecode.starter.captcha.core.sms.ValidateCode;
import com.openbytecode.starter.captcha.exception.ValidateCodeException;
import com.openbytecode.starter.captcha.request.CaptchaGenerateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Base64Utils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author lijunping on 2022/3/29
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/captcha")
public class OpenJobCaptchaController {

    private final ImageCodeGenerator imageCodeGenerator;
    private final SmsCodeGenerator smsCodeGenerator;

    public OpenJobCaptchaController(ImageCodeGenerator imageCodeGenerator, SmsCodeGenerator smsCodeGenerator) {
        this.imageCodeGenerator = imageCodeGenerator;
        this.smsCodeGenerator = smsCodeGenerator;
    }

    @PostMapping("/create/image")
    public Result<OpenJobCaptchaRespDTO> createImageCode(@RequestBody @Valid OpenJobCaptchaRequest request) {
        CaptchaGenerateRequest captchaGenerateRequest = new CaptchaGenerateRequest();
        captchaGenerateRequest.setRequestId(request.getDeviceId());
        try {
            ImageValidateCode imageValidateCode = imageCodeGenerator.create(captchaGenerateRequest);
            return Result.succeed(convert(imageValidateCode));
        } catch (Exception e) {
            return Result.failed(e.getMessage());
        }
    }

    @PostMapping("/create/sms")
    public Result<OpenJobCaptchaRespDTO> createSmsCode(@RequestBody @Valid OpenJobCaptchaRequest request) {
        OpenJobCaptchaRespDTO openJobCaptchaRespDTO = new OpenJobCaptchaRespDTO();
        CaptchaGenerateRequest captchaGenerateRequest = new CaptchaGenerateRequest();
        captchaGenerateRequest.setRequestId(request.getDeviceId());
        try {
            ValidateCode validateCode = smsCodeGenerator.create(captchaGenerateRequest);
            openJobCaptchaRespDTO.setSuccess(true);
            log.info("向手机号: {}发送短信验证码: {}", request.getMobile(), validateCode.getCode());
            return Result.succeed(openJobCaptchaRespDTO);
        } catch (ValidateCodeException e) {
            return Result.failed(e.getMessage());
        }
    }

    private OpenJobCaptchaRespDTO convert(ImageValidateCode imageValidateCode) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(imageValidateCode.getImage(), "JPEG", byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        String base64ImgCode = Base64Utils.encodeToString(bytes);
        OpenJobCaptchaRespDTO openJobCaptchaRespDTO = new OpenJobCaptchaRespDTO();
        openJobCaptchaRespDTO.setImageCode(base64ImgCode);
        openJobCaptchaRespDTO.setSuccess(true);
        return openJobCaptchaRespDTO;
    }

}
