package com.openbytecode.job.admin.openai;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Objects;

/**
 * 描述： sse
 *
 */
@Slf4j
public class DefaultEventSourceListener extends EventSourceListener {

	private final SseEmitter emitter;

	public DefaultEventSourceListener(SseEmitter emitter) {
		this.emitter = emitter;
	}

	@Override
    public void onOpen(EventSource eventSource, Response response) {
		log.info("OpenAI建立sse连接...");
    }

    @Override
    public void onEvent(EventSource eventSource, String id, String type, String data) {
        log.info("OpenAI返回数据：{}", data);

        try {
            if ("[DONE]".equals(data)) {
                log.info("OpenAI返回数据结束了");
                emitter.send(SseEmitter.event().data(data));
                emitter.complete();
                return;
            }

			emitter.send(SseEmitter.event().data(data));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }

    @Override
    public void onClosed(EventSource eventSource) {
        log.info("OpenAI关闭sse连接...");
    }

    @SneakyThrows
    @Override
    public void onFailure(EventSource eventSource, Throwable t, Response response) {
        if(Objects.isNull(response)){
            log.error("OpenAI  sse连接异常:{}", t.getMessage());
            eventSource.cancel();
            return;
        }
        ResponseBody body = response.body();
        if (Objects.nonNull(body)) {
            log.error("OpenAI  sse连接异常data：{}，异常：{}", body.string(), t);
        } else {
            log.error("OpenAI  sse连接异常data：{}，异常：{}", response, t);
        }
        eventSource.cancel();
    }
}
