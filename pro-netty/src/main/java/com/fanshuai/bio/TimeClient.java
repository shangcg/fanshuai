package com.fanshuai.bio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class TimeClient {
    public static void main(String[] args) {

        try {
            SocketAddress address = new InetSocketAddress("127.0.0.1", 6001);
            Socket socket = new Socket();
            socket.connect(address);

            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            writer.println("query time");
            String time = reader.readLine();
            System.out.println(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
