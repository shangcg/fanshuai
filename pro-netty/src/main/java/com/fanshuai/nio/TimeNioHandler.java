package com.fanshuai.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class TimeNioHandler implements Runnable{

    private Selector selector = null;
    private ServerSocketChannel serverChannel = null;
    private volatile boolean stop = false;

    public TimeNioHandler(int port) {
        try {
            selector = Selector.open();

            serverChannel = ServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress(port), 1024);
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("nio server started");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop() {
        this.stop = true;
    }

    public void run() {
        while (!stop) {
            try {
                selector.select(10000);
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();

                    try {
                        handleInput(key);
                    } catch (Exception e) {
                        if (key != null) {
                            key.cancel();
                            if (key.channel() != null) {
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void handleInput(SelectionKey key) throws IOException {
        if (key.isValid()) {
            if (key.isAcceptable()) {
                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                SocketChannel channel = serverSocketChannel.accept();

                channel.configureBlocking(false);
                channel.register(selector, SelectionKey.OP_READ);
            }

            if (key.isReadable()) {
                SocketChannel channel = (SocketChannel) key.channel();
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int bytes = channel.read(buffer);

                if (bytes > 0) {
                    buffer.flip();
                    byte[] data = new byte[buffer.remaining()];
                    buffer.get(data);

                    String message = new String(data);
                    System.out.println("receive message, " + message);

                    String response = "query time".equalsIgnoreCase(message) ? new Date(System.currentTimeMillis()).toString() : "bad message";

                    doWrite(channel, response);
                }

                //未读到数据，继续
                if (bytes == 0) {
                    ;
                }

                if (bytes < 0) {
                    key.channel();
                    channel.close();
                }
            }
        }
    }

    public void doWrite(SocketChannel channel, String msg) throws IOException{
        if (null != msg && msg.length() > 0) {
            byte[] data = msg.getBytes();
            ByteBuffer buffer = ByteBuffer.wrap(data);
            channel.write(buffer);
        }
    }
}
