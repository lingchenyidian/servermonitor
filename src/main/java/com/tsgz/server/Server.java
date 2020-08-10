package com.tsgz.server;

import com.tsgz.common.MonitorThreadFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @Author: liqin
 * @Description:
 * @Date: Created in 10:09 2020/8/10
 * @Modified By:
 */
public class Server {
    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static void addChannel(Channel ch) {
        channels.add(ch);
    }

    public static ChannelGroup getChannels() {
        return channels;
    }

    public static void main(String[] args) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        EventLoopGroup group = new NioEventLoopGroup(1, new MonitorThreadFactory("back-receive-msg"));

        try {
            ChannelFuture future = serverBootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                                 @Override
                                 protected void initChannel(SocketChannel socketChannel) throws Exception {
                                     socketChannel.pipeline().addLast(new ServerChildHandler());
                                 }
                             }

                    )
                    .bind(8888)
                    .sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
