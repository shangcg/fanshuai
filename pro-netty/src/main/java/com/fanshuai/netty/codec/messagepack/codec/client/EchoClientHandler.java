package com.fanshuai.netty.codec.messagepack.codec.client;

import com.fanshuai.netty.codec.messagepack.codec.message.EchoMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoClientHandler extends ChannelInboundHandlerAdapter {
    public void channelActive(ChannelHandlerContext context) {
        EchoMessage echoMessage = new EchoMessage();
        echoMessage.setTitle("hello");
        echoMessage.setText("你好啊");

        for (int i = 0; i < 100; i++) {
            context.write(echoMessage);
        }
        context.flush();

    }

    public void channelRead(ChannelHandlerContext context, Object msg) {

        EchoMessage echoMessage = (EchoMessage) msg;
        System.out.println("receive from server, " + echoMessage);
    }

    public void exceptionCaught(ChannelHandlerContext context, Throwable throwable) {
        context.close();
    }
}
