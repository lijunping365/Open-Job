package com.openbytecode.job.admin.components;

import com.openbytecode.job.admin.entity.OpenJobLogDO;
import com.openbytecode.job.admin.mapper.OpenJobLogMapper;
import com.openbytecode.job.common.domain.MessageBody;
import com.openbytecode.job.common.enums.CommandEnum;
import com.openbytecode.job.common.enums.JobStatusEnum;
import com.openbytecode.job.common.serialize.SerializationUtils;
import com.openbytecode.job.common.time.LocalDateTimeUtil;
import com.openbytecode.rpc.client.intercept.RequestInterceptor;
import com.openbytecode.rpc.core.Message;
import com.openbytecode.rpc.core.enums.PacketType;
import com.openbytecode.rpc.core.transport.MessageRequestBody;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

@Component
public class ScheduleRequestInterceptor implements RequestInterceptor {

    private final OpenJobLogMapper openJobLogMapper;

    public ScheduleRequestInterceptor(OpenJobLogMapper openJobLogMapper) {
        this.openJobLogMapper = openJobLogMapper;
    }

    @Override
    public void intercept(MessageRequestBody messageRequestBody) {
        if (Objects.isNull(messageRequestBody)){
            return;
        }

        Message message = messageRequestBody.getMessage();
        if (message.getCommand() != PacketType.MESSAGE){
            return;
        }

        byte[] body = message.getBody();
        MessageBody messageBody = SerializationUtils.deserialize(body, MessageBody.class);
        CommandEnum command = CommandEnum.of(messageBody.getCommand());
        if (command != CommandEnum.SCHEDULE){
            return;
        }

        String serverId = messageRequestBody.getServerId();
        Long logId = recordLog(serverId, messageBody);
        messageBody.setLogId(logId);
        byte[] serialize = SerializationUtils.serialize(messageBody);
        message.setBody(serialize);
    }

    private Long recordLog(String serverId, MessageBody messageBody){
        OpenJobLogDO openJobLogDO = new OpenJobLogDO();
        LocalDateTime time = LocalDateTimeUtil.toLocalDateTime(messageBody.getStartTime());
        openJobLogDO.setAppId(messageBody.getAppId());
        openJobLogDO.setJobId(messageBody.getJobId());
        openJobLogDO.setServerId(serverId);
        openJobLogDO.setStatus(JobStatusEnum.RUNNING.getValue());
        openJobLogDO.setCreateTime(time);
        openJobLogDO.setStartTime(time);
        openJobLogMapper.insert(openJobLogDO);
        return openJobLogDO.getId();
    }
}
