package com.fanshuai.netty.protocol.server;

import com.fanshuai.netty.protocol.message.Header;
import com.fanshuai.netty.protocol.message.MessageType;
import com.fanshuai.netty.protocol.message.NettyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class HeartBeatReqHandler extends ChannelInboundHandlerAdapter {

    public void channelRead(ChannelHandlerContext context, Object msg) {

        NettyMessage message = (NettyMessage) msg;
        if (message.getHeader() == null) {
            System.out.println("message error, header is null");
            context.close();
        } else {
            Header header = message.getHeader();
            if (header.getType() == MessageType.HEART_BEAT_REQ.getValue()) {
                System.out.println("receive from client heartBeat message: " + message);
                NettyMessage heartBeatResp = buildHeartBeatResp();
                System.out.println("response to client heartBeat message: " + heartBeatResp);
                context.writeAndFlush(heartBeatResp);
            } else {
                context.fireChannelRead(msg);
            }
        }
    }

    public void exceptionCaught(ChannelHandlerContext context, Throwable throwable) {

        context.fireExceptionCaught(throwable);
    }

    public NettyMessage buildHeartBeatResp() {
        NettyMessage nettyMessage = new NettyMessage();

        Header header = new Header();
        header.setType(MessageType.HEART_BEAT_RESP.getValue());

        nettyMessage.setHeader(header);
        return nettyMessage;
    }
}
