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
package com.openbytecode.job.admin.service.impl;

import com.openbytecode.job.admin.entity.OpenJobPrompt;
import com.openbytecode.job.admin.mapper.OpenJobPromptMapper;
import com.openbytecode.job.admin.openai.DefaultEventSourceListener;
import com.openbytecode.job.admin.openai.SseEmitterUTF8;
import com.openbytecode.job.admin.service.OpenJobChatService;
import com.openbytecode.job.api.dto.req.OpenJobChatRequest;
import com.openbytecode.starter.openai.OpenAIClient;
import com.openbytecode.starter.openai.enums.RoleEnum;
import com.openbytecode.starter.openai.exception.OpenAIException;
import com.openbytecode.starter.openai.properties.OpenAIProperties;
import com.openbytecode.starter.openai.utils.JSON;
import com.openbytecode.starter.security.context.UserSecurityContextHolder;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author: 李俊平
 * @Date: 2023-12-30 09:57
 */
@Slf4j
@Service
public class OpenJobChatServiceImpl implements OpenJobChatService {

    private final OkHttpClient okHttpClient;
    private final OpenAIClient openAIClient;
    private final OpenAIProperties properties;
    private final OpenJobPromptMapper promptMapper;

    public OpenJobChatServiceImpl(OkHttpClient okHttpClient,
                                  OpenAIClient openAIClient,
                                  OpenAIProperties properties, OpenJobPromptMapper promptMapper) {
        this.okHttpClient = okHttpClient;
        this.openAIClient = openAIClient;
        this.properties = properties;
        this.promptMapper = promptMapper;
    }

    @Override
    public SseEmitter completionStream(OpenJobChatRequest chatRequest) {
        Long userId = UserSecurityContextHolder.getUserId();

        List<ChatMessage> messages = new ArrayList<>();
        boolean usingContext = chatRequest.isUsingContext();
        if (usingContext){
            addSystemPrompt(messages);
        }
        messages.add(new ChatMessage(RoleEnum.USER.getRoleName(), chatRequest.getPrompt()));

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("moonshot-v1-8k")
                .user(userId.toString())
                .messages(messages)
                .temperature(1.0D)
                .topP(1.0D)
                .stream(true)
                .build();

        SseEmitter emitter = new SseEmitterUTF8(0L);
        try {
            chatCompletionStream(request, new DefaultEventSourceListener(emitter));
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }

        return emitter;
    }

    /**
     * 最新版的GPT-3.5 chat completion 更加贴近官方网站的问答模型 - 流式
     *
     * @param completion 问答参数
     * @return 答案
     */
    private void chatCompletionStream(ChatCompletionRequest completion, EventSourceListener eventSourceListener) {
        try {
            EventSource.Factory factory = EventSources.createFactory(this.okHttpClient);
            String requestBody = JSON.toJSON(completion);
            Request request = new Request.Builder()
                    .url(properties.getBaseUrl() + "v1/chat/completions")
                    .post(RequestBody.create(MediaType.parse("application/json"), requestBody))
                    .build();
            factory.newEventSource(request, eventSourceListener);
        } catch (Exception e) {
            throw new OpenAIException(e.getMessage());
        }
    }

    private void addSystemPrompt(List<ChatMessage> messages){
        OpenJobPrompt prompt = promptMapper.getOne();
        if (Objects.nonNull(prompt) && StringUtils.isNotBlank(prompt.getPrompt())){
            messages.add(new ChatMessage(RoleEnum.SYSTEM.getRoleName(), prompt.getPrompt()));
        }
    }
}
