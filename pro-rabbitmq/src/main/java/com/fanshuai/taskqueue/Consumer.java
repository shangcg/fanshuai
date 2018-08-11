package com.fanshuai.taskqueue;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Consumer {
    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(10);

        ConnectionFactory factory = new ConnectionFactory();
        try {
            Connection connection = factory.newConnection();

            String queueName = "task_queue";
            for (int i=0;i<10;i++) {
                Channel channel = connection.createChannel();
                String name = "consumer" + i;
                UserConsumer consumer = new UserConsumer(channel, queueName);
                consumer.setName(name);
                service.submit(consumer);
            }
        } catch (Exception e) {

        }

    }

    static class UserConsumer implements Runnable{
        private Channel channel = null;
        private String queueName;
        private String name;

        public UserConsumer(Channel channel, String queueName) {
            this.channel = channel;
            this.queueName = queueName;
            initChannel();
        }

        public void setName(String name) {
            this.name = name;
        }

        private void initChannel() {
            try {
                channel.queueDeclare(queueName, true, false, false, null);

                channel.basicConsume(queueName, false, new DefaultConsumer(channel) {
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        String message = new String(body);
                        consumeMessage(message);
                        channel.basicAck(envelope.getDeliveryTag(), false);
                    }
                });
            } catch (Exception e) {
                System.out.println(e+"");
            }

        }

        private void consumeMessage(String message) {

            System.out.println(name + "consume message, " + message);
        }

        public void run() {

        }
    }
}
