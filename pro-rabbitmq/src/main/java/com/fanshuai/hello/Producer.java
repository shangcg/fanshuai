package com.fanshuai.hello;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

public class Producer {
    public static void main(String[] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        String queue_name = "hello";
        channel.queueDeclare(queue_name, true, false, false, null);
        String message = "hello world";
        channel.basicPublish("", queue_name, null, message.getBytes());

        System.out.println(message);
        channel.close();
        connection.close();
    }
}
