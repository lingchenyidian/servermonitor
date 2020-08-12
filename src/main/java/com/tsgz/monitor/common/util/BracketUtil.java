package com.tsgz.monitor.common.util;

/**
 * @Author: liqin
 * @Description:
 * @Date: Created in 11:04 2020/8/5
 * @Modified By:
 */
public class BracketUtil {

    public static String getInnerString(String input) {
        int st = input.indexOf('(');
        int et = input.lastIndexOf(')');
        return st >= 0 && et > st ? input.substring(st + 1, et) : "";
    }


    public static String getInnerDoubleMiddleBracket(String input) {
        String startStr = "[[";
        int st = input.indexOf(startStr);
        int et = input.indexOf("]]");
        return st >= 0 && et > st ? input.substring(st + startStr.length(), et) : "";
    }
}
