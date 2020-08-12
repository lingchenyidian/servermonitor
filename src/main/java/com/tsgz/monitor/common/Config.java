package com.tsgz.monitor.common;

/**
 * @Author: liqin
 * @Description:
 * @Date: Created in 11:25 2020/8/12
 * @Modified By:
 */
public interface Config {
    // 心跳间隔
    int HEARTBEAT_INTERVAL = 10;
    // 进程最大丢失心跳数，超过则被认为该进程不可用的
    int MAX_LOSE_HEARTBEAT_NUM = 5;
    // yarn运行中的任务url
    String YARN_RUNNING_URL = "http://172.16.140.151:8088/cluster/apps/RUNNING";
}
