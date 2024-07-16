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

import com.openbytecode.job.admin.domain.AlarmMessage;
import com.openbytecode.job.admin.entity.OpenJobAlarmRecordDO;
import com.openbytecode.job.admin.entity.OpenJobDO;
import com.openbytecode.job.admin.entity.OpenJobUserDO;
import com.openbytecode.job.admin.mapper.OpenJobAlarmRecordMapper;
import com.openbytecode.job.admin.mapper.OpenJobMapper;
import com.openbytecode.job.admin.mapper.OpenJobUserMapper;
import com.openbytecode.job.admin.service.OpenJobAlarmService;
import com.openbytecode.job.common.time.LocalDateTimeUtil;
import com.openbytecode.starter.alarm.exception.AlarmException;
import com.openbytecode.starter.alarm.provider.dingtalk.DingDingRobotAlarmExecutor;
import com.openbytecode.starter.alarm.provider.dingtalk.DingDingRobotAlarmRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;

/**
 * @author lijunping on 2022/4/25
 */
@Slf4j
@Component
public class OpenJobAlarmServiceImpl implements OpenJobAlarmService {

    @Value("${com.openbytecode.alarm.template}")
    private String alarmTemplate;
    @Value("${com.openbytecode.alarm.night-begin-time}")
    private Integer beginTime;
    @Value("${com.openbytecode.alarm.night-end-time}")
    private Integer endTime;

    private final OpenJobMapper openJobMapper;
    private final OpenJobUserMapper userMapper;
    private final OpenJobAlarmRecordMapper alarmRecordMapper;
    private final DingDingRobotAlarmExecutor alarmExecutor;

    public OpenJobAlarmServiceImpl(OpenJobMapper openJobMapper,
                                   OpenJobUserMapper userMapper,
                                   OpenJobAlarmRecordMapper alarmRecordMapper,
                                   DingDingRobotAlarmExecutor alarmExecutor) {
        this.openJobMapper = openJobMapper;
        this.userMapper = userMapper;
        this.alarmRecordMapper = alarmRecordMapper;
        this.alarmExecutor = alarmExecutor;
    }


    @Override
    public void sendAlarm(AlarmMessage alarmMessage) {
        String cause = alarmMessage.getCause();
        Long takeTime = alarmMessage.getTakeTime();

        OpenJobDO openJobDO = openJobMapper.selectById(alarmMessage.getJobId());
        Long userId = openJobDO.getCreateUser();
        String title = openJobDO.getJobName();
        String startTime = LocalDateTimeUtil.format(alarmMessage.getStartTime(), LocalDateTimeUtil.DATETIME_FORMATTER);
        String finishTime = LocalDateTimeUtil.format(alarmMessage.getFinishTime(), LocalDateTimeUtil.DATETIME_FORMATTER);
        String content = String.format(alarmTemplate, title, startTime, finishTime, takeTime, cause);

        saveAlarmMessage(alarmMessage, content, userId);
        final String hHmm = LocalDateTimeUtil.format(LocalDateTime.now(), "HHmm");
        final int currentTime = Integer.parseInt(hHmm);
        // 控制消息发送时间段，判断是否在合法发送时间
        if (currentTime > beginTime && currentTime < endTime){
            log.warn("不在合法发送时间内，取消本次发送");
            return;
        }

        OpenJobUserDO crawlerUserDO = userMapper.selectById(userId);
        DingDingRobotAlarmRequest request = buildAlarmRequest(content, crawlerUserDO.getPhone());
        send(request);
    }

    private void saveAlarmMessage(AlarmMessage alarmMessage, String message, Long userId){
        OpenJobAlarmRecordDO alarmRecordDO = new OpenJobAlarmRecordDO();
        alarmRecordDO.setAppId(alarmMessage.getAppId());
        alarmRecordDO.setJobId(alarmMessage.getJobId());
        alarmRecordDO.setServerId(alarmMessage.getServerId());
        alarmRecordDO.setMessage(message);
        alarmRecordDO.setReceiver(userId);
        alarmRecordDO.setCreateTime(LocalDateTime.now());
        alarmRecordMapper.insert(alarmRecordDO);
    }

    private DingDingRobotAlarmRequest buildAlarmRequest(String content, String phone){
        // 发送内容
        DingDingRobotAlarmRequest.TextVO text = new DingDingRobotAlarmRequest.TextVO();
        text.setContent(content);

        DingDingRobotAlarmRequest request = new DingDingRobotAlarmRequest();
        // 发送类型
        request.setMsgtype("text");
        request.setText(text);

        // 发送目标
        DingDingRobotAlarmRequest.AtVO at = new DingDingRobotAlarmRequest.AtVO();
        if (StringUtils.isNotBlank(phone)){
            at.setAtMobiles(Collections.singletonList(phone));
        }else {
            at.setIsAtAll(true);
        }
        request.setAt(at);
        return request;
    }

    private void send(DingDingRobotAlarmRequest request){
        try{
            alarmExecutor.sendAlarm(request);
        }catch (AlarmException e){
            log.error("send alarm message failed {}", e.getMessage());
        }
    }
}
