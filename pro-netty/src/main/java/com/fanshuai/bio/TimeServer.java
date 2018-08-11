package com.fanshuai.bio;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TimeServer {
    public static void main(String[] args) {
        String ip = "127.0.0.1";
        int port = 6001;

        ExecutorService executorService = Executors.newFixedThreadPool(30);
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress("127.0.0.1", port));
            System.out.println("server started");

            while (true) {
                Socket socket = serverSocket.accept();

                executorService.execute(new TimeHandler(socket));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
