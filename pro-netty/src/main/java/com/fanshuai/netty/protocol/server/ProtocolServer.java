package com.fanshuai.netty.protocol.server;

import com.fanshuai.netty.protocol.codec.NettyMessageDecoder;
import com.fanshuai.netty.protocol.codec.NettyMessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ProtocolServer {
    private static int port = 6001;

    public void bind(int port) {
        ServerBootstrap bootstrap = new ServerBootstrap();

        EventLoopGroup acceptGroup = new NioEventLoopGroup();
        EventLoopGroup ioGroup = new NioEventLoopGroup();
        try {
            bootstrap.group(acceptGroup, ioGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        public void initChannel(SocketChannel channel) {
                            ChannelPipeline pipeline = channel.pipeline();

                            pipeline.addLast(new NettyMessageEncoder())
                                    .addLast(new NettyMessageDecoder(1024 * 1024, 4, 4))
                                    .addLast(new LoginAuthReqHandler())
                                    .addLast(new HeartBeatReqHandler())
                                    .addLast(new ServerHandler());
                        }
                    });

            ChannelFuture f = bootstrap.bind(port).sync();
            System.out.println("netty server started");

            f.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
            acceptGroup.shutdownGracefully();
            ioGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new ProtocolServer().bind(port);
    }
}
