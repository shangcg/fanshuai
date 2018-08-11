package com.fanshuai.netty.netty04.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

public class TimeServerHandler extends ChannelInboundHandlerAdapter {
    int count = 0;

    public void channelRead(ChannelHandlerContext context, Object msg) {
        String content = (String) msg;

        if (StringUtils.isNotBlank(content)) {
            if (content.equalsIgnoreCase("query time")) {
                System.out.println("receive from client, " + content + ", count= " + count++);

                String response = new Date(System.currentTimeMillis()).toString();
                response = response + System.getProperty("line.separator");
                byte[] data = response.getBytes();
                ByteBuf buf = Unpooled.copiedBuffer(data);
                context.write(buf);
            }
        }
    }

    public void channelReadComplete(ChannelHandlerContext context) {
        context.flush();
    }

    public void exceptionCaught(ChannelHandlerContext context, Throwable throwable) {
        System.out.println(throwable + "");
        context.close();
    }
}
