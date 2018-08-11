package com.fanshuai.netty.netty05.delimeter.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    public void channelRead(ChannelHandlerContext context, Object msg) {
        String content = (String)msg;

        System.out.println("receive from client, " + content);

        String response = content + "###";
        context.write(Unpooled.copiedBuffer(response.getBytes()));
    }

    public void channelReadComplete(ChannelHandlerContext context) {
        context.flush();
    }

    public void exceptionCaught(ChannelHandlerContext context, Throwable throwable) {
        context.close();
    }
}
