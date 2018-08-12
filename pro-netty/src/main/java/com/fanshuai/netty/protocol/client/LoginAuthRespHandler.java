package com.fanshuai.netty.protocol.client;

import com.fanshuai.netty.protocol.message.Body;
import com.fanshuai.netty.protocol.message.Header;
import com.fanshuai.netty.protocol.message.MessageType;
import com.fanshuai.netty.protocol.message.NettyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class LoginAuthRespHandler extends ChannelInboundHandlerAdapter{
    public void channelActive(ChannelHandlerContext context) {

        //生成握手请求
        NettyMessage nettyMessage = buildLoginReq();
        System.out.println("login server: " + nettyMessage);
        context.writeAndFlush(nettyMessage);
    }

    public void channelRead(ChannelHandlerContext context, Object msg) {

        //握手相应  握手成功body返回0  失败返回-1
        NettyMessage loginResp = (NettyMessage) msg;
        if (loginResp.getHeader() != null
                && loginResp.getHeader().getType() == MessageType.LOGIN_RESP.getValue()) {
            Body body = loginResp.getBody();
            if (body.getBody().equals("0")) {
                System.out.println("login is ok:" + loginResp);
                //后续handler处理握手相应
                context.fireChannelRead(msg);
            } else {
                System.out.println("login failed!");
                context.close();
            }
        } else {
            //其他类型的请求让后续的handler处理
            context.fireChannelRead(msg);
        }
    }

    public void exceptionCaught(ChannelHandlerContext context, Throwable throwable) {
        //后续handler处理异常
        context.fireExceptionCaught(throwable);
    }

    public NettyMessage buildLoginReq() {
        NettyMessage nettyMessage = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_REQ.getValue());
        nettyMessage.setHeader(header);

        return nettyMessage;
    }
}
