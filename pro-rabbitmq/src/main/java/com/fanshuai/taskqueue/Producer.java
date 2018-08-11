package com.fanshuai.taskqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class Producer {
    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = null;

        Channel channel = null;
        try {
            connection = factory.newConnection();

            channel = connection.createChannel();
            String queue_name = "task_queue";
            channel.queueDeclare(queue_name, true, false, false, null);
            for (int i=0;i< 1000;i++) {
                String message = "hello, " + i + "th message";
                channel.basicPublish("", queue_name, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
                System.out.println("produce message, " + message);
            }

            channel.close();
            connection.close();
        } catch (Exception e) {
            System.out.println(e+"");
        }
    }
}
