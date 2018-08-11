package com.fanshuai.hello;

import com.rabbitmq.client.*;

public class Consumer {
    public static void main(String[] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();

        String queue_name = "hello";
        Channel channel = connection.createChannel();

        channel.queueDeclare(queue_name, true, false, false, null);

        DefaultConsumer consumer = new DefaultConsumer(channel) {
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] message) {

                String s = new String(message);
                System.out.println(s);
            }
        };
        channel.basicConsume(queue_name, true, consumer);

        channel.close();
        connection.close();
    }
}
