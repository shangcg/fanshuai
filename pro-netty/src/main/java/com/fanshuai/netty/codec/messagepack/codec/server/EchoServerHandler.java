package com.fanshuai.netty.codec.messagepack.codec.server;

import com.fanshuai.netty.codec.messagepack.codec.message.EchoMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    public void channelRead(ChannelHandlerContext context, Object msg) {
        EchoMessage echoMessage = (EchoMessage) msg;

        System.out.println("receive from client: " + echoMessage);
        context.writeAndFlush(echoMessage);
    }

    public void channelReadComplete(ChannelHandlerContext context) {
        context.flush();
    }

    public void exceptionCaught(ChannelHandlerContext context, Throwable throwable) {
        context.close();
        context.channel().closeFuture();
    }
}
