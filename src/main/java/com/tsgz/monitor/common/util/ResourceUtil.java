package com.tsgz.monitor.common.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * @Author: liqin
 * @Description:
 * @Date: Created in 9:33 2020/8/5
 * @Modified By:
 */
public class ResourceUtil {

    public static void close(Closeable... closeables) {
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
