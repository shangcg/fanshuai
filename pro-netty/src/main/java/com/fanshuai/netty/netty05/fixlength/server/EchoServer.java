package com.fanshuai.netty.netty05.fixlength.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class EchoServer {
    public static int port = 6001;

    public void bind(int port) {
        EventLoopGroup acceptGroup = new NioEventLoopGroup();
        EventLoopGroup ioGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(acceptGroup, ioGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new Handler());

            System.out.println("netty server started");

            ChannelFuture f = bootstrap.bind(port).sync();
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class Handler extends ChannelInitializer<SocketChannel> {
        public void initChannel(SocketChannel channel) {
            channel.pipeline()
                    .addLast(new FixedLengthFrameDecoder(20))
                    .addLast(new StringDecoder())
                    .addLast(new EchoServerHandler());
        }
    }

    public static void main(String[] args) {
        new EchoServer().bind(6001);
    }
}
