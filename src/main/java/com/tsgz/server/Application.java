package com.tsgz.server;

import com.tsgz.common.MonitorThreadFactory;
import com.tsgz.server.parser.YarnUIParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.concurrent.*;

/**
 * @Author: liqin
 * @Description:
 * @Date: Created in 17:24 2020/8/5
 * @Modified By:
 */
public class Application {
    public static void main(String[] args) {

        // 后台打印线程池
        ScheduledExecutorService pool1 = Executors.newScheduledThreadPool(1, new MonitorThreadFactory("print"));
        pool1.scheduleAtFixedRate(
                () -> MonitorManager.getInstance().print(),
                1, 10, TimeUnit.SECONDS
        );

        // 后台获取SparkOnYarnAppInfo的线程池
        ScheduledExecutorService pool2 = Executors.newScheduledThreadPool(1, new MonitorThreadFactory("getappinfo"));
        pool2.scheduleAtFixedRate(
                () -> {
                    String url = "http://172.16.140.151:8088/cluster/apps/RUNNING";
                    try {
                        Document doc = Jsoup.connect(url).get();
                        YarnUIParser.getInstance().parseUI(doc, null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                },
                0, 10, TimeUnit.SECONDS
        );

        // TODO 后台与client通信的线程池
        ExecutorService serverPool = Executors.newSingleThreadExecutor(new MonitorThreadFactory("accept-client"));
        serverPool.submit(() -> {

        });
    }
}
