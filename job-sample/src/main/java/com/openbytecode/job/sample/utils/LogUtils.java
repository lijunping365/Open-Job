package com.openbytecode.job.sample.utils;

import com.openbytecode.job.common.log.JobLogResult;
import com.openbytecode.job.common.time.LocalDateTimeUtil;
import com.openbytecode.starter.logger.LoggerContext;
import com.openbytecode.starter.logger.util.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.time.LocalDateTime;

/**
 * @author: 李俊平
 * @Date: 2023-07-29 16:48
 */
@Slf4j
public class LogUtils {

    /**
     * log filename, like "logPath/yyyy-MM-dd/logId.log"
     *
     * @param triggerDate
     * @param logId
     * @return
     */
    public static String makeLogFileName(LocalDateTime triggerDate, long logId) {
        String format = LocalDateTimeUtil.format(triggerDate, LocalDateTimeUtil.DATE_FORMATTER);
        File logFilePath = new File(LoggerContext.getLogBasePath(), format);
        if (!logFilePath.exists()) {
            logFilePath.mkdir();
        }

        // filePath/yyyy-MM-dd/jobId.log
        String logFileName = logFilePath.getPath()
                .concat(File.separator)
                .concat(String.valueOf(logId))
                .concat(".log");
        return logFileName;
    }

    /**
     * support read log-file
     *
     * @param logFileName
     * @return log content
     */
    public static JobLogResult readLog(String logFileName, int fromLineNum){
        String[] logContent = FileUtil.readFileLine(logFileName, fromLineNum);
        if (logContent == null || logContent.length == 0){
            return new JobLogResult(fromLineNum, 0, "readLog fail, logFile not found", true);
        }
        return new JobLogResult(fromLineNum, Integer.parseInt(logContent[1]), logContent[0], false);
    }
}
