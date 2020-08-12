package com.tsgz.monitor.common;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: liqinDe
 * @Description:
 * @Date: Created in 17:42 2020/8/5
 * @Modified By:
 */
public class MonitorThreadFactory implements ThreadFactory {
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private AtomicInteger threadNumber = new AtomicInteger(1);
    private ThreadGroup group;
    private String namePrefix;
    public MonitorThreadFactory(String use) {
        SecurityManager s = System.getSecurityManager();
        group = s != null ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        namePrefix = "monitor-pool-" + poolNumber.getAndIncrement() + "-" + use + "-";
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(group, r, namePrefix + threadNumber.getAndIncrement());
        return thread;
    }
}
