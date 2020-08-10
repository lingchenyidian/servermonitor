package com.tsgz.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * @Author: liqin
 * @Description:
 * @Date: Created in 10:18 2020/8/10
 * @Modified By:
 */
public class ServerChildHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = null;

        buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];

        buf.getBytes(buf.readerIndex(), bytes);

        System.out.println(new String(bytes));

        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.pipeline().channel().remoteAddress();
        System.out.println(remoteAddress.getAddress().toString());
        System.out.println(remoteAddress.getPort());
        Server.getChannels().writeAndFlush(msg);

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Server.addChannel(ctx.channel());
    }


}
