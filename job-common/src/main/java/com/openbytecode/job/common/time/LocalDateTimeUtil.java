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
package com.openbytecode.job.common.time;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author: 李俊平
 * @Date: 2022-02-26 15:32
 */
public interface LocalDateTimeUtil {

    String DATETIME_FORMATTER = "yyyy-MM-dd HH:mm:ss";

    String DATE_FORMATTER = "yyyy-MM-dd";

    String TIME_FORMATTER = "HH:mm:ss";

    /**
     * 将 毫秒级 时间戳转化为 {@link LocalDateTime}
     *
     * @param milliseconds milliseconds 毫秒级时间戳
     * @return {@link LocalDateTime}
     */
    static LocalDateTime toLocalDateTime(long milliseconds) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneId.systemDefault());
    }

    /**
     * Date转换为LocalDateTime
     */
    static LocalDateTime convertDateToLDT(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * LocalDateTime转换为Date
     *
     * @param time
     * @return
     */
    static Date convertLDTToDate(LocalDateTime time) {
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 将时间字符串转为自定义时间格式的LocalDateTime
     * @param time 需要转化的时间字符串
     * @param format 自定义的时间格式
     * @return
     */
    static LocalDateTime parse(String time, String format) {
        return LocalDateTime.parse(time, DateTimeFormatter.ofPattern(format));
    }

    /**
     * 将LocalDateTime转为自定义的时间格式的字符串
     * @param localDateTime 需要转化的LocalDateTime
     * @param format 自定义的时间格式
     * @return
     */
    static String format(LocalDateTime localDateTime, String format) {
        return localDateTime.format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * 获取指定日期的毫秒
     *
     * @param time
     * @return
     */
    static Long getMillis(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 获取当天的00:00:00
     *
     * @return
     */
    static LocalDateTime getDayStart(LocalDateTime time) {
        return time.withHour(0).withMinute(0).withSecond(0);
    }

    /**
     * 获取当天的23:59:59
     *
     * @return
     */
    static LocalDateTime getDayEnd(LocalDateTime time) {
        return time.withHour(23).withMinute(59).withSecond(59);
    }

    /**
     * 计算两个时间之间的差值
     * @param start
     * @param end
     * @return
     */
    static String getTimeBetween(LocalDateTime start, LocalDateTime end){
        //获取秒数
        long nowSecond = end.toEpochSecond(ZoneOffset.ofHours(0));
        long endSecond = start.toEpochSecond(ZoneOffset.ofHours(0));
        long absSeconds = Math.abs(nowSecond - endSecond);
        //获取秒数
        long s = absSeconds % 60;
        //获取分钟数
        long m = absSeconds / 60 % 60;
        //获取小时数
        long h = absSeconds / 60 / 60 % 24;
        //获取天数
        long d = absSeconds / 60 / 60 / 24;

        return d + "天" + h + "时" + m + "分" + s + "秒";
    }
}
