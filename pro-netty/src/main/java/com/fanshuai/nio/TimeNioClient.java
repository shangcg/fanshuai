package com.fanshuai.nio;

public class TimeNioClient {
    public static void main(String[] args) {
        Runnable runnable = new TimeNioClientHandler();

        new Thread(runnable).start();
    }
}
