package com.fanshuai.netty.netty05.delimeter.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class EchoServer {
    private static int port = 6001;

    public void bind(int port) throws Exception{

        EventLoopGroup acceptGroup = new NioEventLoopGroup();
        EventLoopGroup ioGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(acceptGroup, ioGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new Handler());

            ChannelFuture f = bootstrap.bind(port).sync();
            System.out.println("netty server started");

            f.channel().closeFuture().sync();
        } finally {
            acceptGroup.shutdownGracefully();
            ioGroup.shutdownGracefully();
        }
    }

    private class Handler extends ChannelInitializer<SocketChannel> {
        public void initChannel(SocketChannel channel) {
            String delimeter = "###";
            ByteBuf buf = Unpooled.copiedBuffer(delimeter.getBytes());

            channel.pipeline()
                    .addLast(new DelimiterBasedFrameDecoder(1024, buf))
                    .addLast(new StringDecoder())
                    .addLast(new EchoServerHandler());
        }
    }

    public static void main(String[] args) {
        try {
            new EchoServer().bind(port);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
