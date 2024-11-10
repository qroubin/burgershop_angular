package com.burgers.burgers.restaurant;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.burgers.burgers.RabbitMQHelper;
import com.rabbitmq.client.DeliverCallback;

public class Restaurant {

    public static void main(String[] argv) throws Exception {

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Restaurant received '" + message + "'");
            System.out.println("Commande en cuisine...");
            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            executor.schedule(() -> {
                System.out.println("Commande prête!");
                RabbitMQHelper.sendToQueue(RabbitMQHelper.QUEUE2_NAME, message);
            }, 5, TimeUnit.SECONDS);
        };

        RabbitMQHelper.receiveFromQueue(RabbitMQHelper.QUEUE1_NAME, deliverCallback);
    }
}
