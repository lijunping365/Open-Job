package com.openbytecode.job.common.metrics;

import lombok.Data;

/**
 * 系统指标信息
 */
@Data
public class SystemMetricsInfo {
    /**
     * CPU processor num.
     */
    private int cpuProcessors;
    /**
     * Percent of CPU load.
     */
    private double cpuLoad;

    /**
     * Memory that is used by JVM, in GB.
     */
    private double jvmUsedMemory;
    /**
     * Max memory that JVM can use, in GB.
     */
    private double jvmMaxMemory;
    /**
     * Ratio of memory that JVM uses to total memory, 0.X,
     * the value is between 0 and 1.
     */
    private double jvmMemoryUsage;

    /**
     * Total used disk space, in GB.
     */
    private double diskUsed;
    /**
     * Total disk space, in GB.
     */
    private double diskTotal;
    /**
     * Used disk ratio.
     */
    private double diskUsage;
}
