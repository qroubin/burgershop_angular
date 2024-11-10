package com.burgers.burgers.order;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository repository;

    private SimpMessagingTemplate messagingTemplate;

    public OrderService(OrderRepository repository, SimpMessagingTemplate messagingTemplate) {
        this.repository = repository;
        this.messagingTemplate = messagingTemplate;
    }

    public Order updateOrder(Order newOrder, Long id) {
        Order updatedOrder = repository.findById(id)
        .map(order -> {
            if(newOrder.getBurgers() != null) order.setBurgers(newOrder.getBurgers());
            if(newOrder.getIsProcessed() != null) order.setIsProcessed(newOrder.getIsProcessed());
            return repository.save(order);
        })
        .orElseThrow(() -> new OrderNotFoundException(id));
        if(updatedOrder != null) messagingTemplate.convertAndSend("/orderWs/value", id);
        return updatedOrder;
    }
    
}
