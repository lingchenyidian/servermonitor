package com.tsgz.monitor.server.parser;

import com.tsgz.monitor.common.entity.AbstractAppInfo;
import org.jsoup.nodes.Document;

/**
 * @Author: liqin
 * @Description:
 * @Date: Created in 10:59 2020/8/5
 * @Modified By:
 */
public interface HtmlParser<T extends AbstractAppInfo> extends Parser {
    void parseUI(Document doc, T t);
}
