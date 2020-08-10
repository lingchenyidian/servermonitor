package com.tsgz.client;

import com.tsgz.common.MonitorThreadFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * @Author: liqin
 * @Description:
 * @Date: Created in 11:22 2020/8/7
 * @Modified By:
 */
public class Client {
    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup(1, new MonitorThreadFactory("back-send-msg"));

        Bootstrap bootstrap = new Bootstrap();

        ChannelFuture future = bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new ClientHandler());
                    }
                })
                .connect("localhost", 8888);

        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    System.out.println("success");
                } else {
                    System.out.println("failed");
                }
            }
        });

        System.out.println("...before...");
        try {
            future.sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("...after...");

    }

    private static class ClientHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {

            ByteBuf byteBuf = Unpooled.copiedBuffer("appName".getBytes());
            ctx.writeAndFlush(byteBuf);
        }


    }
}
