package com.fanshuai.netty.netty05.fixlength.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    public void channelActive(ChannelHandlerContext context) {
        System.out.println("new channel active");
    }

    public void channelRead(ChannelHandlerContext context, Object msg) {
        String content = (String) msg;
        System.out.println("from client:" + content);
    }
}
