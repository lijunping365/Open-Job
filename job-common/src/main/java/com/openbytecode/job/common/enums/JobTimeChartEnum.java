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
package com.openbytecode.job.common.enums;

import com.openbytecode.job.common.exception.ServiceException;
import com.openbytecode.job.common.vo.ResultEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

/**
 * @author: 李俊平
 * @Date: 2023-03-21 14:00
 */
@Getter
@AllArgsConstructor
public enum JobTimeChartEnum {

    NEARLY_1M("1m", 1, "最近1分钟"),

    NEARLY_30M("30m", 30,"最近30分钟"),

    NEARLY_1H("1h",1, "最近1小时"),

    NEARLY_24H("24h",24, "最近24小时"),

    NEARLY_7D("7d",7, "最近7天"),

    NEARLY_30D("30d",30, "最近30天"),

    NEARLY_90D("90d",90, "最近90天"),

    ;

    private final String code;

    private final Integer value;

    private final String name;


    public static JobTimeChartEnum of(String code){
        for (JobTimeChartEnum routeStrategyEnum : JobTimeChartEnum.values()) {
            if (StringUtils.equals(routeStrategyEnum.getCode(), code)){
                return routeStrategyEnum;
            }
        }
        throw new ServiceException(ResultEnum.BUSINESS_EXCEPTION);
    }

    public static LocalDateTime getStartTime(LocalDateTime now, String code){
        JobTimeChartEnum timeEnum = of(code);
        switch (timeEnum){
            case NEARLY_1M:
            case NEARLY_30M:
                return now.plusMinutes(-timeEnum.getValue());
            case NEARLY_1H:
            case NEARLY_24H:
                return now.plusHours(-timeEnum.getValue());
            case NEARLY_7D:
            case NEARLY_30D:
            case NEARLY_90D:
                return now.plusDays(-timeEnum.getValue());
            default:
                return now.plusMinutes(-1);
        }
    }
}
