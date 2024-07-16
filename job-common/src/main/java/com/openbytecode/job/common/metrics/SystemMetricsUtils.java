package com.openbytecode.job.common.metrics;


import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.math.RoundingMode;
import java.text.NumberFormat;

public class SystemMetricsUtils {

    private static final NumberFormat NF = NumberFormat.getNumberInstance();
    static {
        NF.setMaximumFractionDigits(4);
        NF.setMinimumFractionDigits(4);
        NF.setRoundingMode(RoundingMode.HALF_UP);
        NF.setGroupingUsed(false);
    }

    private static final Runtime runtime = Runtime.getRuntime();

    private static final OperatingSystemMXBean osMXBean = ManagementFactory.getOperatingSystemMXBean();

    /**
     * CPU processor num.
     */
    public static int getCPUProcessorNum(){
        return osMXBean.getAvailableProcessors();
    }

    /**
     * Percent of CPU load.
     */
    public static double getCPULoadPercent(){
        return formatDouble(osMXBean.getSystemLoadAverage());
    }

    /**
     * maxMemory指JVM能从操作系统获取的最大内存，即-Xmx参数设置的值
     */
    public static double getJvmMaxMemory(){
        long maxMemory = runtime.maxMemory();
        return transformation(maxMemory);
    }

    /**
     * 获取已使用内存：当前申请总量 - 当前空余量
     * totalMemory指JVM当前持久的总内存
     */
    public static double getJvmUsedMemory(){
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        return (transformation(usedMemory));
    }

    /**
     * 已用内存比例
     */
    public static double getJvmMemoryUsage(double usedMemory, double maxMemory){
        return formatDouble((double) usedMemory / maxMemory);
    }

    /**
     * Total used disk space, in GB.
     */
    public static double getDiskUsed(long total, long free){
        return transformation(total - free);
    }

    /**
     * Total disk space, in GB.
     */
    public static double getDiskTotal(long total){
        return transformation(total);
    }

    /**
     * Used disk ratio.
     */
    public static double getDiskUsage(double diskUsed, double diskTotal){
        return formatDouble(diskUsed / diskTotal);
    }

    /**
     * 获取磁盘使用情况
     */
    public static long[] getDiskInfo(){
        long free = 0;
        long total = 0;
        File[] roots = File.listRoots();
        for (File file : roots) {
            free += file.getFreeSpace();
            total += file.getTotalSpace();
        }
        return new long[]{free, total};
    }

    /**
     * 将字节容量转化为GB
     */
    public static double transformation(long size){
        return formatDouble(size / 1024.0 / 1024 / 1024);
    }

    private static double formatDouble(double origin) {
        return Double.parseDouble(NF.format(origin));
    }

}
