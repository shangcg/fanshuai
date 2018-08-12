package com.fanshuai.netty.protocol.client;

import com.fanshuai.netty.protocol.codec.NettyMessageDecoder;
import com.fanshuai.netty.protocol.codec.NettyMessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ProtocolClient {
    private static String ip = "127.0.0.1";
    private static int port = 6001;

    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    public void connect(String ip, int port) {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        public void initChannel(SocketChannel socketChannel) {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new NettyMessageEncoder())
                                    .addLast(new NettyMessageDecoder(1024 * 1024, 4, 4))
                                    .addLast(new LoginAuthRespHandler())
                                    .addLast(new HeartBeatRespHandler());
                        }
                    });

            ChannelFuture f = bootstrap.connect(ip, port).sync();
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            executorService.execute(new RetryConnectTask());
        }
    }

    private class RetryConnectTask implements Runnable {
        //连接失败时每5秒 重连一次
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(5);
                connect(ip, port);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new ProtocolClient().connect(ip, port);
    }
}
