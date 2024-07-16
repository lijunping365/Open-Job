package com.openbytecode.job.sample.components;

import com.openbytecode.starter.job.register.param.HandlerParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author: 李俊平
 * @Date: 2024-01-20 14:59
 */
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class JobHandlerParam extends HandlerParam {

    /**
     * The id of job
     */
    private Long jobId;
    /**
     * The params of job
     */
    private String params;
    /**
     * The script of job
     */
    private String script;
    /**
     * The lang of script
     */
    private String scriptLang;
    /**
     * The update time of script
     */
    private String scriptUpdateTime;
    /**
     * 任务分片节点列表
     */
    private List<String> shardingNodes;
}
