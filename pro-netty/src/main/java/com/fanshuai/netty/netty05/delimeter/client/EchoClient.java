package com.fanshuai.netty.netty05.delimeter.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class EchoClient {
    private static String ip = "127.0.0.1";
    private static int port = 6001;

    public void connect() throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new Handler());

            System.out.println("netty client started");

            ChannelFuture f = bootstrap.connect(ip, port).sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    private static class Handler extends ChannelInitializer<SocketChannel> {
        public void initChannel(SocketChannel channel) {
            String delimeter = "###";
            ByteBuf buf = Unpooled.copiedBuffer(delimeter.getBytes());
            channel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, buf))
                    .addLast(new StringDecoder())
                    .addLast(new EchoClientHandler());
        }
    }

    public static void main(String[] args) throws Exception{
        new EchoClient().connect();
    }
}
