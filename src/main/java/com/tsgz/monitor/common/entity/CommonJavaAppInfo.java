package com.tsgz.monitor.common.entity;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Author: liqin
 * @Description:
 * @Date: Created in 13:05 2020/8/5
 * @Modified By:
 */
public class CommonJavaAppInfo extends AbstractAppInfo {

    private static volatile CommonJavaAppInfo instance = null;

    public static CommonJavaAppInfo getInstance(String appName) {
        if (instance == null) {
            synchronized (CommonJavaAppInfo.class) {
                if (instance == null) {
                    instance = new CommonJavaAppInfo(appName);
                }
            }
        }
        instance.init();
        return instance;
    }

    private CommonJavaAppInfo(String appName) {
        this.name = appName;
    }


    public void init() {
        try {
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            this.id = hostAddress + ":" + System.getProperty("user.dir");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        // 初始化堆内存信息
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        this.initHeapSize = byteToMByte(heapMemoryUsage.getInit());
        this.usedHeapSize = byteToMByte(heapMemoryUsage.getUsed());
        this.maxHeapSize = byteToMByte(heapMemoryUsage.getMax());

        // 初始化非堆内存信息
        MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();
        this.initNonHeapSize = byteToMByte(nonHeapMemoryUsage.getInit());
        this.usedNonHeapSize = byteToMByte(nonHeapMemoryUsage.getUsed());
        this.maxNonHeapSize = byteToMByte(nonHeapMemoryUsage.getMax());

        // 初始化操作系统信息
        OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        this.totalMSize = byteToMByte(operatingSystemMXBean.getTotalPhysicalMemorySize());
        this.freeMSize = byteToMByte(operatingSystemMXBean.getFreePhysicalMemorySize());
        this.committedVirtualMemorySize = byteToMByte(operatingSystemMXBean.getCommittedVirtualMemorySize());
        this.cpuLoad = String.format("%2.2f", operatingSystemMXBean.getProcessCpuLoad() * 100) + "%";
    }


    // 堆内存信息
    private int initHeapSize;
    private int maxHeapSize;
    private int usedHeapSize;

    // 非堆内存信息
    private int initNonHeapSize;
    private int maxNonHeapSize;
    private int usedNonHeapSize;

    private int totalMSize;
    private int freeMSize;
    private int committedVirtualMemorySize;
    private String cpuLoad;


    public int getInitHeapSize() {
        return initHeapSize;
    }

    public void setInitHeapSize(int initHeapSize) {
        this.initHeapSize = initHeapSize;
    }

    public int getMaxHeapSize() {
        return maxHeapSize;
    }

    public void setMaxHeapSize(int maxHeapSize) {
        this.maxHeapSize = maxHeapSize;
    }

    public int getUsedHeapSize() {
        return usedHeapSize;
    }

    public void setUsedHeapSize(int usedHeapSize) {
        this.usedHeapSize = usedHeapSize;
    }

    public int getInitNonHeapSize() {
        return initNonHeapSize;
    }

    public void setInitNonHeapSize(int initNonHeapSize) {
        this.initNonHeapSize = initNonHeapSize;
    }

    public int getMaxNonHeapSize() {
        return maxNonHeapSize;
    }

    public void setMaxNonHeapSize(int maxNonHeapSize) {
        this.maxNonHeapSize = maxNonHeapSize;
    }

    public int getUsedNonHeapSize() {
        return usedNonHeapSize;
    }

    public void setUsedNonHeapSize(int usedNonHeapSize) {
        this.usedNonHeapSize = usedNonHeapSize;
    }

    public int getTotalMSize() {
        return totalMSize;
    }

    public void setTotalMSize(int totalMSize) {
        this.totalMSize = totalMSize;
    }

    public int getFreeMSize() {
        return freeMSize;
    }

    public void setFreeMSize(int freeMSize) {
        this.freeMSize = freeMSize;
    }

    public int getCommittedVirtualMemorySize() {
        return committedVirtualMemorySize;
    }

    public void setCommittedVirtualMemorySize(int committedVirtualMemorySize) {
        this.committedVirtualMemorySize = committedVirtualMemorySize;
    }

    public String getCpuLoad() {
        return cpuLoad;
    }

    public void setCpuLoad(String cpuLoad) {
        this.cpuLoad = cpuLoad;
    }

    private static int byteToMByte(long num) {
        return (int) (num >> 20);
    }
}
