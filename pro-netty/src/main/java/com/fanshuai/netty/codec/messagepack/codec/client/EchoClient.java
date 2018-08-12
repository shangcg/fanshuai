package com.fanshuai.netty.codec.messagepack.codec.client;

import com.fanshuai.netty.codec.messagepack.codec.server.MsgPackDecoder;
import com.fanshuai.netty.codec.messagepack.codec.server.MsgPackEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

public class EchoClient {
    private static String ip = "127.0.0.1";
    private static int port = 6001;

    public void connect(String ip, int port) {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        public void initChannel(SocketChannel channel) {
                            channel.pipeline()
                                    //每个消息的前面加2字节的长度字段
                                    .addLast(new LengthFieldPrepender(2))
                                    .addLast(new MsgPackEncoder())

                                    //解析消息时，从头部提取2个字节的长度字段
                                    .addLast(new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2))
                                    .addLast(new MsgPackDecoder())
                                    .addLast(new EchoClientHandler());
                        }
                    });

            System.out.println("netty client started");

            ChannelFuture f = bootstrap.connect(ip, port).sync();
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new EchoClient().connect(ip, port);
    }
}
