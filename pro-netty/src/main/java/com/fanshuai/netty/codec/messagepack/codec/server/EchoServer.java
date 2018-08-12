package com.fanshuai.netty.codec.messagepack.codec.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.LineBasedFrameDecoder;

public class EchoServer {
    private static int port = 6001;

    public void bind(int port) {
        EventLoopGroup acceptGroup = new NioEventLoopGroup();
        EventLoopGroup ioGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(acceptGroup, ioGroup)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        public void initChannel(SocketChannel channel) {

                            channel.pipeline()
                                    //每个消息的前面加2字节的长度字段
                                    .addLast(new LengthFieldPrepender(2))
                                    .addLast(new MsgPackEncoder())

                                    //解析消息时，从头部提取2个字节的长度字段
                                    .addLast(new LengthFieldBasedFrameDecoder(65535, 0,2, 0, 2))
                                    .addLast(new MsgPackDecoder())
                                    .addLast(new EchoServerHandler());
                        }
                    });

            System.out.println("netty server started");

            ChannelFuture f = bootstrap.bind(port).sync();
            f.channel().closeFuture().sync();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            acceptGroup.shutdownGracefully();
            ioGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new EchoServer().bind(port);
    }
}
