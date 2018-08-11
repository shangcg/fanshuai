package com.fanshuai.netty.netty04.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeClientHandler extends ChannelInboundHandlerAdapter {
    byte[] data = null;
    int count = 0;

    public TimeClientHandler() {
        String msg = "query time" + System.getProperty("line.separator");
        data = msg.getBytes();
    }
    public void channelActive(ChannelHandlerContext context) {
        int length = data.length;
        ByteBuf buf = Unpooled.buffer(length * 100);

        for (int i = 0; i < 100; i++) {
            buf.writeBytes(data);
        }
        context.writeAndFlush(buf);
    }

    public void channelRead(ChannelHandlerContext context, Object msg) {
        String content = (String) msg;
        System.out.println("receive from server, " + content + ", count = " + count++);
    }

    public void exceptionCaught(ChannelHandlerContext context, Throwable throwable) {
        context.close();
    }
}
