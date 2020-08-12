package com.tsgz.monitor.server;

import com.tsgz.monitor.common.MonitorThreadFactory;
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
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
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

    public static void removeChannel(Channel ch) {
        channels.remove(ch);
    }


    public static ChannelGroup getChannels() {
        return channels;
    }

    public static void run() {
        //1.创建ServerBootstrap实例
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        EventLoopGroup group = new NioEventLoopGroup(1, new MonitorThreadFactory("back-receive-msg"));

        try {
            ChannelFuture future = serverBootstrap
                    //2.设置并绑定Reactor线程池。Netty的Reactor线程池是EventLoopGroup
                    .group(group)
                    //3.设置并绑定服务端的channel
                    .channel(NioServerSocketChannel.class)
                    //4.链路创建的时候创建并初始化ChannelPipeline
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                                 @Override
                                 protected void initChannel(SocketChannel socketChannel) throws Exception {
                                     socketChannel.pipeline()
                                             .addLast(new LengthFieldBasedFrameDecoder(65535, 0, 4, 0, 4))
                                             .addLast(
                                                     new ObjectDecoder(
                                                             ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader()))
                                             )
                                             .addLast(new LengthFieldPrepender(4))
                                             .addLast(new ObjectEncoder())
                                             //5.初始化ChannelPipeline完成之后，添加并设置ChannelHandler
                                             .addLast(new ServerHandler());
                                 }
                             }

                    )
                    //6.绑定端口
                    .bind(8888)
                    .sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
