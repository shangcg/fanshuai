package com.fanshuai.bio;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

public class TimeHandler implements Runnable {
    private Socket socket = null;

    public TimeHandler(Socket socket) {
        this.socket = socket;
        System.out.println("new timehandler init");
    }

    public void run() {
        BufferedReader reader = null;
        PrintWriter writer = null;

        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            while (true) {
                String line = reader.readLine();
                if (StringUtils.isBlank(line)) {
                    break;
                }

                System.out.println("receive client message, " + line);
                if (line.equalsIgnoreCase("query time")) {
                    writer.println(new Date(System.currentTimeMillis()));
                } else {
                    writer.println("bad message");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                reader.close();
                writer.close();
                socket.close();
            } catch (Exception e1) {
                e.printStackTrace();
            }
        }
    }
}
