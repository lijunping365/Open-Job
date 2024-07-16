package com.openbytecode.job.api.dto.create;

import lombok.Data;

/**
 * @author: 李俊平
 * @Date: 2023-12-22 08:41
 */
@Data
public class OpenInstanceCreateDTO {

    /**
     * 应用 id
     */
    private Long appId;
    /**
     * ip 地址
     */
    private String serverId;

}
