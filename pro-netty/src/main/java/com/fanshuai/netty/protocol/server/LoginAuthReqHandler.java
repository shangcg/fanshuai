package com.fanshuai.netty.protocol.server;

import com.fanshuai.netty.protocol.message.Body;
import com.fanshuai.netty.protocol.message.Header;
import com.fanshuai.netty.protocol.message.MessageType;
import com.fanshuai.netty.protocol.message.NettyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoginAuthReqHandler extends ChannelInboundHandlerAdapter {
    //已登录ip列表
    private Map<String, Boolean> loginList = new ConcurrentHashMap<>();
    //ip地址白名单
    private String[] whiteList = new String[] {"127.0.0.1", "192.168.1.115"};

    public void channelRead(ChannelHandlerContext context, Object msg) {
        NettyMessage loginReq = (NettyMessage) msg;
        System.out.println("login request from client: " + loginReq);

        if (loginReq.getHeader() != null
                && loginReq.getHeader().getType() == MessageType.LOGIN_REQ.getValue()) {
            String nodeIndex = context.channel().remoteAddress().toString();

            NettyMessage loginResp = null;
            if (loginList.containsKey(nodeIndex)) {
                //已登录用户握手失败，防止重复登录
                loginResp = buildLoginResp("-1");
            } else {
                InetSocketAddress address = (InetSocketAddress) context.channel().remoteAddress();
                String ip = address.getAddress().getHostAddress();
                boolean ok = false;
                for (String wip : whiteList) {
                    if (wip.equals(ip)) {
                        ok = true;
                        break;
                    }
                }

                loginResp = buildLoginResp(ok ? "0" : "-1");

            }
            System.out.println("login response is: " + loginResp);
            context.writeAndFlush(loginResp);
        } else {
            //其他类型的请求让后续handler处理
            context.fireChannelRead(msg);
        }
    }

    public void exceptionCaught(ChannelHandlerContext context, Throwable throwable) {

        //登录异常， 删除已登录记录，删除链接
        loginList.remove(context.channel().remoteAddress().toString());
        context.close();

        context.fireExceptionCaught(throwable);
    }

    //生成握手相应
    public NettyMessage buildLoginResp(String result) {
        NettyMessage nettyMessage = new NettyMessage();

        Header header = new Header();
        header.setType(MessageType.LOGIN_RESP.getValue());

        Body body = new Body();
        body.setBody(result);

        nettyMessage.setHeader(header);
        nettyMessage.setBody(body);

        return nettyMessage;
    }
}
