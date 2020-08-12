package com.tsgz.monitor.client;

import com.tsgz.monitor.common.MonitorThreadFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;


/**
 * @Author: liqin
 * @Description:
 * @Date: Created in 11:22 2020/8/7
 * @Modified By:
 */
public class Client {
    private static EventLoopGroup group = new NioEventLoopGroup(1, new MonitorThreadFactory("back-send-msg"));

    public static void main(String[] args) {

        Bootstrap bootstrap = new Client().buildBootstrap(group);
    }

    Bootstrap buildBootstrap(EventLoopGroup group) {
        Bootstrap bootstrap = new Bootstrap();
        final ClientHandler handler = new ClientHandler(this);

        try {
            ChannelFuture future = bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new LengthFieldBasedFrameDecoder(65535, 0, 4, 0, 4))
                                    .addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(this.getClass().getClassLoader())))
                                    .addLast(new LengthFieldPrepender(4))
                                    .addLast(new ObjectEncoder())
                                    .addLast(new IdleStateHandler(3, 2, 6, TimeUnit.SECONDS))
                                    .addLast(handler);
                        }
                    })
                    .connect("localhost", 8888)
                    .addListener(new ConnectionListener(this)).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return bootstrap;
    }

} 
