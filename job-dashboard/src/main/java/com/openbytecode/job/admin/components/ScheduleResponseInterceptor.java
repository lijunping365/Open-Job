package com.openbytecode.job.admin.components;

import com.openbytecode.job.admin.entity.OpenJobLogDO;
import com.openbytecode.job.admin.event.JobLogEvent;
import com.openbytecode.job.common.domain.MessageBody;
import com.openbytecode.job.common.enums.CommandEnum;
import com.openbytecode.job.common.enums.JobStatusEnum;
import com.openbytecode.job.common.serialize.SerializationUtils;
import com.openbytecode.job.common.time.LocalDateTimeUtil;
import com.openbytecode.rpc.client.intercept.ResponseInterceptor;
import com.openbytecode.rpc.core.Message;
import com.openbytecode.rpc.core.enums.PacketType;
import com.openbytecode.rpc.core.enums.ResponseStatus;
import com.openbytecode.rpc.core.transport.MessageRequestBody;
import com.openbytecode.rpc.core.transport.MessageResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ScheduleResponseInterceptor implements ResponseInterceptor {

    private final ApplicationEventPublisher eventPublisher;

    public ScheduleResponseInterceptor(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }


    @Override
    public void intercept(MessageRequestBody request, MessageResponseBody response) {
        Message message = request.getMessage();
        if (Objects.isNull(response) || message.getCommand() != PacketType.MESSAGE ){
            return;
        }

        byte[] body = message.getBody();
        MessageBody messageBody = SerializationUtils.deserialize(body, MessageBody.class);
        CommandEnum command = CommandEnum.of(messageBody.getCommand());
        if (command != CommandEnum.SCHEDULE){
            return;
        }

        updateLog(messageBody, response);
    }

    private void updateLog(MessageBody messageBody, MessageResponseBody response){
        Long startTime = messageBody.getStartTime();
        long endTime = System.currentTimeMillis();
        Long logId = messageBody.getLogId();

        String errMsg = null;
        if (Objects.nonNull(response) && response.getStatus() != ResponseStatus.SUCCESS) {
            errMsg = response.getMsg();
        }

        OpenJobLogDO openJobLogUpdateDTO = new OpenJobLogDO();
        openJobLogUpdateDTO.setId(logId);
        openJobLogUpdateDTO.setStatus(StringUtils.isBlank(errMsg) ? JobStatusEnum.SUCCEED.getValue() : JobStatusEnum.FAILED.getValue());
        openJobLogUpdateDTO.setCause(errMsg);
        openJobLogUpdateDTO.setFinishTime(LocalDateTimeUtil.toLocalDateTime(endTime));
        openJobLogUpdateDTO.setTakeTime(endTime - startTime);

        JobLogEvent logEvent = new JobLogEvent(this, openJobLogUpdateDTO);
        eventPublisher.publishEvent(logEvent);
    }
}
