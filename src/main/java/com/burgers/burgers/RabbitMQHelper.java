package com.burgers.burgers;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Connection;

import java.io.IOException;

import com.rabbitmq.client.Channel;

public class RabbitMQHelper {
    private static ConnectionFactory factory = new ConnectionFactory();
    private static Connection connection;
    private static Channel channel;

    public static String RABBITMQ_HOST = "localhost";
    public static String QUEUE1_NAME = "inProcessing";
    public static String QUEUE2_NAME = "processed";

    private static void setupFactory() {
        factory.setHost(RABBITMQ_HOST);
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(QUEUE1_NAME, false, false, false, null);
            channel.queueDeclare(QUEUE2_NAME, false, false, false, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendToQueue(String queueName, String messageToSend) {
        setupFactory();
        try {
            channel.basicPublish("", queueName, null, messageToSend.getBytes());
            System.out.println(" [x] API sent '" + messageToSend + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void receiveFromQueue(String queueName, DeliverCallback callbackFunction) {
        setupFactory();
        System.out.println(" [*] API waiting for messages. To exit press CTRL+C");

        try {
            channel.basicConsume(queueName, true, callbackFunction, consumerTag -> { });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
