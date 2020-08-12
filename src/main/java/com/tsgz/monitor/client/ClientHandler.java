package com.tsgz.monitor.client;

import com.tsgz.monitor.common.entity.CommonJavaAppInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.concurrent.TimeUnit;

/**
 * @Author: liqin
 * @Description:
 * @Date: Created in 17:10 2020/8/11
 * @Modified By:
 */

public class ClientHandler extends ChannelInboundHandlerAdapter {
    private Client client;
    private String mainClassName;

    public ClientHandler(Client client) {
        this.client = client;

        // 获取主类的名称，必须是在main方法调用的方法才能用此法获取
        StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();
        int stackSize = stackTrace.length;
        for (int i = 0; i < stackSize; i++) {
            if ("main".equals(stackTrace[i].getMethodName())) {
                this.mainClassName = stackTrace[i].getClassName();
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        ByteBuf byteBuf = Unpooled.copiedBuffer("appName".getBytes());
//        ctx.writeAndFlush(byteBuf);
        super.channelActive(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
//        ByteBuf byteBuf = Unpooled.copiedBuffer("register".getBytes());
//        ctx.writeAndFlush(byteBuf);
//        System.out.println("注册");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        EventLoop loop = ctx.channel().eventLoop();
        long delay = 10L;
        System.out.println(String.format("monitor server断开连接 %ds后重连...", delay));
        loop.schedule(() -> client.buildBootstrap(loop), delay, TimeUnit.SECONDS);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;

            switch (event.state()) {
                case READER_IDLE:
                    System.out.println("长期没收到服务器心跳返回数据...");
                    break;
                case WRITER_IDLE:
                    System.out.println("向服务器发送心跳数据");
                    ctx.writeAndFlush(CommonJavaAppInfo.getInstance(this.mainClassName));
//                    ctx.writeAndFlush(Unpooled.copiedBuffer("heartbeat".getBytes()));
                    break;
                case ALL_IDLE:
                    System.out.println("ALL");
            }
        }
    }
}
