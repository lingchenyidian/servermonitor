package com.tsgz.server.parser;

/**
 * @Author: liqin
 * @Description:
 * @Date: Created in 11:00 2020/8/5
 * @Modified By:
 */
public interface CommandParser extends Parser {
    void parseCommand(String command);
}
