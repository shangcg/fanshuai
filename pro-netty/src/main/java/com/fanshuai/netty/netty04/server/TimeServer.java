package com.fanshuai.netty.netty04.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class TimeServer {

    private static int port = 6001;

    public void bind(int port) throws Exception{
        EventLoopGroup acceptGroup = new NioEventLoopGroup();
        EventLoopGroup ioGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(acceptGroup, ioGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChildHandler());

            ChannelFuture f = bootstrap.bind(port).sync();
            System.out.println("netty server started");

            f.channel().closeFuture().sync();
        } finally {
            acceptGroup.shutdownGracefully();
            ioGroup.shutdownGracefully();
        }

    }

    private class ChildHandler extends ChannelInitializer<SocketChannel> {
        public void initChannel(SocketChannel channel) {
            channel.pipeline()
                    .addLast(new LineBasedFrameDecoder(1024))
                    .addLast(new StringDecoder())
                    .addLast(new TimeServerHandler());
        }
    }

    public static void main(String[] args){
        try {
            new TimeServer().bind(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
