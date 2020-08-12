package com.tsgz.monitor.client;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;

import java.util.concurrent.TimeUnit;

/**
 * @Author: liqin
 * @Description:
 * @Date: Created in 16:41 2020/8/11
 * @Modified By:
 */
public class ConnectionListener implements ChannelFutureListener {
    private Client client;

    public ConnectionListener(Client client) {
        this.client = client;
    }

    @Override
    public void operationComplete(ChannelFuture future) throws Exception {
        if (future.isSuccess()) {
            System.out.println("monitor server连接成功!!!");
        } else {
            long delay = 10L;
            System.out.println(String.format("启动未连接到monitor server %ds后重连...", delay));

            EventLoop loop = future.channel().eventLoop();
            loop.schedule(() -> client.buildBootstrap(loop), delay, TimeUnit.SECONDS);
        }
    }
}
