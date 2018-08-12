package com.fanshuai.netty.protocol.client;

import com.fanshuai.netty.protocol.message.Header;
import com.fanshuai.netty.protocol.message.MessageType;
import com.fanshuai.netty.protocol.message.NettyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.ScheduledFuture;

import java.util.concurrent.TimeUnit;

public class HeartBeatRespHandler extends ChannelInboundHandlerAdapter {
    private volatile ScheduledFuture<?> heartBeatFuture;

    public void channelRead(ChannelHandlerContext context, Object msg) {

        NettyMessage message = (NettyMessage) msg;
        if (message.getHeader() == null) {
            System.out.println("message error, header is null");
            context.close();
        } else {
            Header header = message.getHeader();

            if (header.getType() == MessageType.LOGIN_RESP.getValue()) {
                //握手成功，开始周期性发送心跳消息

                heartBeatFuture = context.executor().scheduleAtFixedRate(new HeartBeatTask(context), 0, 5000, TimeUnit.MILLISECONDS);
            } else if (header.getType() == MessageType.HEART_BEAT_RESP.getValue()) {
                System.out.println("receive heartBeat from server: " + message);
            } else {
                //其他类型的消息交给下一个handler
                context.fireChannelRead(msg);
            }
        }
    }

    public void exceptionCaught(ChannelHandlerContext context, Throwable throwable) {

        //心跳异常，则停止发心跳消息
        if (heartBeatFuture != null) {
            heartBeatFuture.cancel(true);
            heartBeatFuture = null;
        }
        context.fireExceptionCaught(throwable);
    }

    public NettyMessage buildHeartBeat() {
        NettyMessage nettyMessage = new NettyMessage();

        Header header = new Header();
        header.setType(MessageType.HEART_BEAT_REQ.getValue());

        nettyMessage.setHeader(header);
        return nettyMessage;
    }

    private class HeartBeatTask implements Runnable {
        private final ChannelHandlerContext context;

        public HeartBeatTask(ChannelHandlerContext context) {
            this.context = context;
        }

        public void run() {
            NettyMessage heartBeat = buildHeartBeat();
            context.writeAndFlush(heartBeat);
        }
    }
}
