package com.burgers.burgers.order;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.burgers.burgers.burger.BurgerRepository;
import com.burgers.burgers.burger.Burger;
import com.burgers.burgers.burger.BurgerNotFoundException;

import com.rabbitmq.client.DeliverCallback;

import com.burgers.burgers.RabbitMQHelper;

@RestController
public class OrderController {
    private final OrderRepository repository;

    private final BurgerRepository burgerRepository;

    private final OrderModelAssembler assembler;

    private OrderService orderService;

    OrderController(OrderRepository repository, BurgerRepository burgerRepository,
                        OrderModelAssembler assembler, OrderService orderService) {
        this.repository = repository;
        this.burgerRepository = burgerRepository;
        this.assembler = assembler;
        this.orderService = orderService;
    }

    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");
        Long orderId = Long.parseLong(message);
        System.out.println(" [x] API received '" + message + "'");
        Order newOrder = new Order();
        newOrder.setIsProcessed(true);
        try {
            Order updatedOrder = orderService.updateOrder(newOrder, orderId);
            System.out.println("API updated order " + orderId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    };

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/orders")
    CollectionModel<EntityModel<Order>> all() {
        List<EntityModel<Order>> orders = repository.findAll().stream()
        .map(assembler::toModel)
        .collect(Collectors.toList());

        return CollectionModel.of(orders, linkTo(methodOn(OrderController.class).all()).withSelfRel());
    }
    // end::get-aggregate-root[]

    // Single item
    
    @GetMapping("/orders/{id}")
    EntityModel<Order> one(@PathVariable Long id) {
        
        Order order = repository.findById(id)
        .orElseThrow(() -> new OrderNotFoundException(id));

        return assembler.toModel(order);
    }

    @PostMapping("/orders")
    ResponseEntity<?> newOrder(@RequestBody List<Long> burgerIds) {
        List<Burger> burgers = burgerRepository.findAllById(burgerIds);
        if(burgers.size() < burgerIds.size()){
            for (Long burgerId : burgerIds) {
                boolean isFound = false;
                for (Burger b : burgers) {
                    if(b.getId() == burgerId){
                        isFound = true;
                        break;
                    }
                }
                if(!isFound) throw new BurgerNotFoundException(burgerId);
            }
        }
        Order newOrder = new Order(burgers);
        EntityModel<Order> entityModel = assembler.toModel(repository.save(newOrder));
        RabbitMQHelper.sendToQueue(RabbitMQHelper.QUEUE1_NAME, Long.toString(newOrder.getId()));
        RabbitMQHelper.receiveFromQueue(RabbitMQHelper.QUEUE2_NAME, deliverCallback);
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }
    
    @PutMapping("/orders/{id}")
    ResponseEntity<?> replaceOrder(@RequestBody Order newOrder, @PathVariable Long id) {
        
        Order replacedOrder = repository.findById(id)
        .map(order -> {
            order.setBurgers(newOrder.getBurgers());
            order.setIsProcessed(newOrder.getIsProcessed());
            return repository.save(order);
        })
        .orElseGet(() -> {
            newOrder.setId(id);
            return repository.save(newOrder);
        });
        EntityModel<Order> entityModel = assembler.toModel(replacedOrder);
        return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel);
    }

    @PatchMapping("/orders/{id}")
    ResponseEntity<?> patchOrder(@RequestBody Order newOrder, @PathVariable Long id) {
        
        Order updatedOrder = repository.findById(id)
        .map(order -> {
            if(newOrder.getBurgers() != null) order.setBurgers(newOrder.getBurgers());
            if(newOrder.getIsProcessed() != null) order.setIsProcessed(newOrder.getIsProcessed());
            return repository.save(order);
        })
        .orElseThrow(() -> new OrderNotFoundException(id));
        EntityModel<Order> entityModel = assembler.toModel(updatedOrder);
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/orders/{id}")
    ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        if(repository.existsById(id)){
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            throw new OrderNotFoundException(id);
        }
    }
}
