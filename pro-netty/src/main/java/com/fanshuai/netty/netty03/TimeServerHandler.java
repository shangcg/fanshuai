package com.fanshuai.netty.netty03;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

public class TimeServerHandler extends ChannelInboundHandlerAdapter {
    public void channelRead(ChannelHandlerContext context, Object msg) throws Exception{
        ByteBuf buf = (ByteBuf) msg;
        byte[] data = new byte[buf.readableBytes()];

        buf.readBytes(data);
        String content = new String(data);
        System.out.println("receive from client, " + content);

        String response = content.equalsIgnoreCase("query time") ? new Date(System.currentTimeMillis()).toString() : "bad message";

        ByteBuf writeBuf = Unpooled.copiedBuffer(response.getBytes());
        context.write(writeBuf);
    }

    public void channelReadComplete(ChannelHandlerContext context) throws Exception{
        context.flush();
    }

    public void exceptionCaught(ChannelHandlerContext context, Throwable throwable) {
        context.close();
    }
}
