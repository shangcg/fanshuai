package com.fanshuai.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class TimeNioClientHandler implements Runnable{
    private String ip = "127.0.0.1";
    private int port = 6002;

    private Selector selector;
    private SocketChannel channel;
    private volatile boolean stop = false;

    public TimeNioClientHandler() {
        try {
            selector = Selector.open();

            channel = SocketChannel.open();
            channel.configureBlocking(false);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void doConnect() throws IOException{
        if (channel.connect(new InetSocketAddress(ip, port))) {
            channel.register(selector, SelectionKey.OP_READ);
            doWrite();
        } else {
            channel.register(selector, SelectionKey.OP_CONNECT);
        }
    }

    public void doWrite() throws IOException{
        String msg = "query time";
        ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
        channel.write(buffer);
    }

    public void run() {
        try {
            doConnect();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
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
                    } catch (IOException e) {
                        if (key != null) {
                            key.cancel();
                            if (key.channel() != null) {
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void handleInput(SelectionKey key) throws IOException{

        if (key.isValid()) {
            SocketChannel sc = (SocketChannel) key.channel();

            if (key.isConnectable()) {
                if (sc.finishConnect()) {
                    sc.register(selector, SelectionKey.OP_READ);
                    doWrite();
                } else {
                    System.exit(1);
                }
            }

            if (key.isReadable()) {
                ByteBuffer buffer = ByteBuffer.allocate(1024);

                int bytes = sc.read(buffer);
                if (bytes > 0) {
                    buffer.flip();
                    byte[] data = new byte[buffer.remaining()];
                    buffer.get(data);
                    String msg = new String(data);

                    System.out.println("receive from server, " + msg);

                    this.stop = true;
                } else if (bytes == 0) {
                    ;
                } else {
                    key.channel();
                    channel.close();
                }
            }
        }
    }
}
