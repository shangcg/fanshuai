package com.fanshuai.netty.netty05.delimeter.client;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoClientHandler extends ChannelInboundHandlerAdapter {
    public void channelActive(ChannelHandlerContext context) {
        String msg = "hello world";
        msg += "###";
        context.writeAndFlush(Unpooled.copiedBuffer(msg.getBytes()));
    }

    public void channelRead(ChannelHandlerContext context, Object msg) {
        String content = (String) msg;
        System.out.println("recelve from server," + content);
    }

    public void exceptionCaught(ChannelHandlerContext context, Throwable throwable) {
        context.close();
        context.channel().close();
    }
}
