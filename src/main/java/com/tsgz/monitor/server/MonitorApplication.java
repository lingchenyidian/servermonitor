package com.tsgz.monitor.server;

import com.tsgz.monitor.common.Config;
import com.tsgz.monitor.common.MonitorThreadFactory;
import com.tsgz.monitor.common.entity.CommonJavaAppInfo;
import com.tsgz.monitor.common.util.ResourceUtil;
import com.tsgz.monitor.server.parser.YarnUIParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author: liqin
 * @Description:
 * @Date: Created in 17:24 2020/8/5
 * @Modified By:
 */
public class MonitorApplication {
    public static void main(String[] args) {

        // 1.后台打印线程池
        ScheduledExecutorService pool1 = Executors.newScheduledThreadPool(1, new MonitorThreadFactory("print"));
        pool1.scheduleAtFixedRate(
                () -> {
                    updateOwnAppInfo();
                    MonitorManager.getInstance().print();
                },
                1, 10, TimeUnit.SECONDS
        );

        // 2.后台获取SparkOnYarnAppInfo的线程池
        ScheduledExecutorService pool2 = Executors.newScheduledThreadPool(1, new MonitorThreadFactory("getappinfo"));
        pool2.scheduleAtFixedRate(
                () -> {
                    String url = Config.YARN_RUNNING_URL;
                    try {
                        Document doc = Jsoup.connect(url).get();
                        YarnUIParser.getInstance().parseUI(doc, null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                },
                0, 10, TimeUnit.SECONDS
        );

        // 3.后台与client通信的线程池
        Server.run();

        // 4.后台判断某个进程是否可用，并重启的线程池
        ScheduledExecutorService monitorPool = Executors.newScheduledThreadPool(1, new MonitorThreadFactory("monitor"));
        monitorPool.scheduleAtFixedRate(() -> {
            MonitorManager.getInstance().getApps().forEach(
                    (id, app) -> {

                        int loseHeartbeatNum = (int) (System.currentTimeMillis() - app.getUpdateTime().getTime()) / 1000 / Config.HEARTBEAT_INTERVAL;
                        app.setLoseHeartbeatNum(loseHeartbeatNum);
                        if (loseHeartbeatNum >= 0 * Config.MAX_LOSE_HEARTBEAT_NUM) {
                            BufferedReader br = null;
                            try {
                                Process process = Runtime.getRuntime().exec("ipconfig");
                                br = new BufferedReader(new InputStreamReader(process.getInputStream()));
                                String line;
                                while ((line = br.readLine()) != null) {
                                    System.out.println(line);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                ResourceUtil.close(br);
                            }
                        }
                    }
            );
        }, 1, 10, TimeUnit.SECONDS);

        // 更新自身的应用信息
        updateOwnAppInfo();
    }

    private static void updateOwnAppInfo() {
        // 将自身加入到MonitorManager中
        CommonJavaAppInfo localInfo = CommonJavaAppInfo.getInstance(MonitorApplication.class.getName());
        localInfo.setId("localhost:" + System.getProperty("user.dir"));
        localInfo.setUpdateTime(new Date());

        MonitorManager.getInstance().updateAppInfo(localInfo);
    }
}
