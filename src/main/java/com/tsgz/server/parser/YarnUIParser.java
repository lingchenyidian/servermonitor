package com.tsgz.server.parser;

import com.tsgz.common.entity.SparkOnYarnAppInfo;
import com.tsgz.common.util.BracketUtil;
import com.tsgz.server.MonitorManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * @Author: liqin
 * @Description:
 * @Date: Created in 11:16 2020/8/5
 * @Modified By:
 */
public class YarnUIParser implements HtmlParser<SparkOnYarnAppInfo> {
    private YarnUIParser() {}
    private volatile static YarnUIParser instance = null;

    public static YarnUIParser getInstance() {
        if (instance == null) {
            synchronized (YarnUIParser.class) {
                if (instance == null) {
                    instance = new YarnUIParser();
                }
            }
        }
        return instance;
    }


    public void parseUI(Document doc, SparkOnYarnAppInfo appInfo) {
        Elements scripts = doc.getElementsByTag("script");

//        Element x = scripts.get(6);
        scripts.stream().filter(e -> e.data().contains("appsTableData=[")).findFirst().ifPresent(
                x -> {
                    String data = x.data();
                    data = data.replaceAll("\n", "");
                    String string = BracketUtil.getInnerDoubleMiddleBracket(data);
                    for (String apps : string.split("],\\[")) {
                        String[] attrs = apps.split("\",\"");
//                        System.out.println(apps);
                        Document a1 = Jsoup.parse(attrs[0].substring(1));
//                        System.out.println(a1.text());
                        SparkOnYarnAppInfo sparkOnYarnAppInfo = new SparkOnYarnAppInfo(attrs[2], a1.text());

                        String lastAttr = attrs[attrs.length - 1];
                        Document a2 = Jsoup.parse(lastAttr.substring(0, lastAttr.length() - 1));
                        String url = a2.getElementsByTag("a").get(0).attr("href");

                        try {
                            Document document = Jsoup.connect(url + "streaming").get();
                            SparkUIParser.getInstance().parseUI(document, sparkOnYarnAppInfo);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        MonitorManager.getInstance().updateAppInfo(sparkOnYarnAppInfo);
                    }
                }
        );
    }
}
