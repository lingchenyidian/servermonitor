package com.tsgz.monitor.server;

import com.tsgz.monitor.common.entity.CommonJavaAppInfo;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetSocketAddress;
import java.util.Date;

/**
 * @Author: liqin
 * @Description:
 * @Date: Created in 10:18 2020/8/10
 * @Modified By:
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof CommonJavaAppInfo) {
            CommonJavaAppInfo javaAppInfo = (CommonJavaAppInfo) msg;

            // 更新应用信息
            if (javaAppInfo.getId() == null) {
                InetSocketAddress remoteAddress = (InetSocketAddress) ctx.pipeline().channel().remoteAddress();
                javaAppInfo.setId(remoteAddress.toString());
                System.out.println(String.format("客户端name = %s, id为空, 强制赋予id=%s", javaAppInfo.getName(), javaAppInfo.getId()));
            }
            javaAppInfo.setUpdateTime(new Date());

            MonitorManager.getInstance().updateAppInfo(javaAppInfo);
        } else {
            ByteBuf buf = null;
            buf = (ByteBuf) msg;
            byte[] bytes = new byte[buf.readableBytes()];

            buf.getBytes(buf.readerIndex(), bytes);
            System.out.println(new String(bytes));
        }

        Server.getChannels().writeAndFlush("yes");
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        printClientIpAndPort(ctx, true);
        Server.addChannel(ctx.channel());
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        printClientIpAndPort(ctx, false);
        Server.removeChannel(ctx.channel());

    }

    private void printClientIpAndPort(ChannelHandlerContext ctx, boolean online) {
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.pipeline().channel().remoteAddress();
        String clientIp = remoteAddress.getAddress().toString();
        int port = remoteAddress.getPort();
        System.out.println(String.format("client：[%s:%d] %s!!!", clientIp.substring(1), port, online ? "上线":"下线"));
    }
}
