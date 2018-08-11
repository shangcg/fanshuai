package com.fanshuai.netty.netty04.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;


public class TimeClient {
    public static String ip = "127.0.0.1";
    public static int port = 6001;

    public void connect(String ip, int port) throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .handler(new Handler());

            ChannelFuture f = bootstrap.connect(ip, port).sync();
            System.out.println("netty client started");

            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    private class Handler extends ChannelInitializer<SocketChannel> {
        public void initChannel(SocketChannel channel) throws Exception{
            channel.pipeline()
                    .addLast(new LineBasedFrameDecoder(1024))
                    .addLast(new StringDecoder())
                    .addLast(new TimeClientHandler());
        }
    }

    public static void main(String[] args) {
        try {
            new TimeClient().connect(ip, port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
