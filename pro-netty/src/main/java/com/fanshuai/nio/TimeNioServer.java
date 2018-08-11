package com.fanshuai.nio;

public class TimeNioServer {

    public static void main(String[] args) {

        int port = 6002;

        TimeNioHandler handler = new TimeNioHandler(port);
        new Thread(handler).start();
    }
}
