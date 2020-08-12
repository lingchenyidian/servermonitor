package com.tsgz.monitor.server.parser;

import com.tsgz.monitor.common.entity.SparkOnYarnAppInfo;
import com.tsgz.monitor.common.util.BracketUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * @Author: liqin
 * @Description:
 * @Date: Created in 10:59 2020/8/5
 * @Modified By:
 */
public class SparkUIParser implements HtmlParser<SparkOnYarnAppInfo> {
    private SparkUIParser() {}
    private volatile static SparkUIParser instance = null;

    public static SparkUIParser getInstance() {
        if (instance == null) {
            synchronized (SparkUIParser.class) {
                if (instance == null) {
                    instance = new SparkUIParser();
                }
            }
        }
        return instance;
    }

    public void parseUI(Document doc, SparkOnYarnAppInfo appInfo) {
        Element active = doc.getElementById("active");
        Element completed = doc.getElementById("completed");
//        System.out.println(active.text());
//        System.out.println(completed.text());

        String activeBatches = BracketUtil.getInnerString(active.text());
        String[] completedBracketArr = BracketUtil.getInnerString(completed.text()).split(" ");
        String completedBatches = completedBracketArr[completedBracketArr.length - 1];
        appInfo.setActiveBatch(Long.parseLong(activeBatches));
        appInfo.setCompletedBatch(Long.parseLong(completedBatches));
    }
}
