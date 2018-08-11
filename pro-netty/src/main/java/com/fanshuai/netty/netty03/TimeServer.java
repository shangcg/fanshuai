package com.fanshuai.netty.netty03;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TimeServer {
    private static int port = 6003;

    public void bind(int port) {
        EventLoopGroup acceptGroup = new NioEventLoopGroup();
        EventLoopGroup ioGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(acceptGroup, ioGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new TimeHandler());

            ChannelFuture future = bootstrap.bind(port).sync();
            System.out.println("netty server started");

            //等待链路关闭
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            acceptGroup.shutdownGracefully();
            ioGroup.shutdownGracefully();
        }
    }

    private class TimeHandler extends ChannelInitializer<SocketChannel> {
        public void initChannel(SocketChannel socketChannel) throws Exception{
            socketChannel.pipeline().addLast(new TimeServerHandler());
        }
    }

    public static void main(String[] args) {
        new TimeServer().bind(port);
    }
}
