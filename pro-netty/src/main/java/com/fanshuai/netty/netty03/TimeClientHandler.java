package com.fanshuai.netty.netty03;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeClientHandler extends ChannelInboundHandlerAdapter {

    public void channelActive(ChannelHandlerContext context) throws Exception{
        String msg = "query time";
        for (int i = 0; i< 100; i++) {
            byte[] data = msg.getBytes();
            ByteBuf buf = Unpooled.buffer(data.length);
            buf.writeBytes(data);
            context.writeAndFlush(buf);
        }

    }

    public void channelRead(ChannelHandlerContext context, Object msg) throws Exception{
        ByteBuf buf = (ByteBuf) msg;

        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);

        String content = new String(data);
        System.out.println("receive from server, " + content);
        //关闭链路
        context.close();
    }

    public void exceptionCaught(ChannelHandlerContext context, Throwable throwable) {
        context.close();
    }
}
