package com.burgers.burgers.order;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.burgers.burgers.burger.Burger;
import com.burgers.burgers.burger.BurgerRepository;

@SpringBootTest
//@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Transactional      // this reverts changes after each test
public class OrderRepositoryTests {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private BurgerRepository burgerRepository;

    @BeforeEach
    public void setup() {
        orderRepository.deleteAll(); // Delete all data
        burgerRepository.deleteAll(); // Delete all data, including burgers from the LoadDatabase class
    }

    @Test
    public void orderRepository_deleteAll_deletesAllOrders() {
        ArrayList<String> ingredientsTestBurger = new ArrayList<>();
        ingredientsTestBurger.addAll(Arrays.asList("cheddar", "poulet"));
        Burger burger = new Burger("testBurger", ingredientsTestBurger);
        Burger savedBurger = burgerRepository.save(burger);
        Order order1 = new Order(Arrays.asList(savedBurger));
        Order order2 = new Order(Arrays.asList(savedBurger));
        orderRepository.save(order1);
        orderRepository.save(order2);

        orderRepository.deleteAll();
        List<Order> ordersFound = orderRepository.findAll();
        
        Assertions.assertThat(ordersFound.size()).isEqualTo(0);
    }

    @Test
    public void orderRepository_save_returnSavedOrder() {
        ArrayList<String> ingredientsTestBurger = new ArrayList<>();
        ingredientsTestBurger.addAll(Arrays.asList("cheddar", "steak"));
        Burger burger = new Burger("testBurger", ingredientsTestBurger);
        Burger savedBurger = burgerRepository.save(burger);
        Order order = new Order(Arrays.asList(savedBurger));

        Order savedOrder = orderRepository.save(order);

        Assertions.assertThat(savedOrder).isNotNull();
        Assertions.assertThat(savedOrder.getId()).isGreaterThan(0);
    }

    @Test
    public void orderRepository_findAllById_returnSearchedOrders() {
        ArrayList<String> ingredientsTestBurger1 = new ArrayList<>();
        ingredientsTestBurger1.addAll(Arrays.asList("cheddar", "poulet"));
        Burger burger1 = new Burger("testBurger1", ingredientsTestBurger1);
        Burger savedBurger1 = burgerRepository.save(burger1);
        ArrayList<String> ingredientsTestBurger2 = new ArrayList<>();
        ingredientsTestBurger2.addAll(Arrays.asList("cheddar", "salad"));
        Burger burger2 = new Burger("testBurger2", ingredientsTestBurger2);
        Burger savedBurger2 = burgerRepository.save(burger2);
        ArrayList<String> ingredientsTestBurger3 = new ArrayList<>();
        ingredientsTestBurger3.addAll(Arrays.asList("cheddar", "steak", "oignons"));
        Burger burger3 = new Burger("testBurger3", ingredientsTestBurger3);
        Burger savedBurger3 = burgerRepository.save(burger3);
        Order order1 = new Order(Arrays.asList(savedBurger1));
        Order order2 = new Order(Arrays.asList(savedBurger2,savedBurger3));
        Order order3 = new Order(Arrays.asList(savedBurger1, savedBurger3));
        Order savedOrder1 = orderRepository.save(order1);
        orderRepository.save(order2);
        Order savedOrder3 = orderRepository.save(order3);

        List<Order> ordersFound = orderRepository.findAllById(Arrays.asList(savedOrder1.getId(), savedOrder3.getId()));
        
        Assertions.assertThat(ordersFound.size()).isEqualTo(2);
        Assertions.assertThat(ordersFound.get(0).getId()).isEqualTo(savedOrder1.getId());
        Assertions.assertThat(ordersFound.get(1).getId()).isEqualTo(savedOrder3.getId());
    }

    @Test
    public void orderRepository_findAll_returnSearchedOrders() {
        ArrayList<String> ingredientsTestBurger1 = new ArrayList<>();
        ingredientsTestBurger1.addAll(Arrays.asList("cheddar", "poulet"));
        Burger burger1 = new Burger("testBurger1", ingredientsTestBurger1);
        Burger savedBurger1 = burgerRepository.save(burger1);
        ArrayList<String> ingredientsTestBurger2 = new ArrayList<>();
        ingredientsTestBurger2.addAll(Arrays.asList("cheddar", "steak", "oignons"));
        Burger burger2 = new Burger("testBurger2", ingredientsTestBurger2);
        Burger savedBurger2 = burgerRepository.save(burger2);
        Order order1 = new Order(Arrays.asList(savedBurger1));
        Order order2 = new Order(Arrays.asList(savedBurger1, savedBurger2));
        Order savedOrder1 = orderRepository.save(order1);
        Order savedOrder2 = orderRepository.save(order2);

        List<Order> ordersFound = orderRepository.findAll();

        Assertions.assertThat(ordersFound.size()).isEqualTo(2);
        Assertions.assertThat(ordersFound.get(0).getId()).isEqualTo(savedOrder1.getId());
        Assertions.assertThat(ordersFound.get(1).getId()).isEqualTo(savedOrder2.getId());
    }

    @Test
    public void orderRepository_existsById_returnTrueForRightId() {
        ArrayList<String> ingredientsTestBurger = new ArrayList<>();
        ingredientsTestBurger.addAll(Arrays.asList("cheddar", "poulet"));
        Burger burger = new Burger("testBurger", ingredientsTestBurger);
        Burger savedBurger = burgerRepository.save(burger);
        Order order = new Order(Arrays.asList(savedBurger));
        Order savedOrder = orderRepository.save(order);

        Boolean isOrderFound = orderRepository.existsById(savedOrder.getId());

        Assertions.assertThat(isOrderFound).isTrue();
    }

    @Test
    public void orderRepository_existsById_returnFalseForWrongId() {
        ArrayList<String> ingredientsTestBurger = new ArrayList<>();
        ingredientsTestBurger.addAll(Arrays.asList("cheddar", "poulet"));
        Burger burger = new Burger("testBurger", ingredientsTestBurger);
        Burger savedBurger = burgerRepository.save(burger);
        Order order = new Order(Arrays.asList(savedBurger));
        orderRepository.save(order);

        Boolean isOrderFound = orderRepository.existsById(99l);

        Assertions.assertThat(isOrderFound).isFalse();
    }

    @Test
    public void orderRepository_findById_returnSearchedOrder() {
        ArrayList<String> ingredientsTestBurger1 = new ArrayList<>();
        ingredientsTestBurger1.addAll(Arrays.asList("cheddar", "poulet"));
        Burger burger1 = new Burger("testBurger1", ingredientsTestBurger1);
        Burger savedBurger1 = burgerRepository.save(burger1);
        ArrayList<String> ingredientsTestBurger2 = new ArrayList<>();
        ingredientsTestBurger2.addAll(Arrays.asList("cheddar", "salad"));
        Burger burger2 = new Burger("testBurger2", ingredientsTestBurger2);
        Burger savedBurger2 = burgerRepository.save(burger2);
        Order order1 = new Order(Arrays.asList(savedBurger1));
        Order order2 = new Order(Arrays.asList(savedBurger1, savedBurger2));
        orderRepository.save(order1);
        Order savedOrder2 = orderRepository.save(order2);

        Order orderFound = orderRepository.findById(savedOrder2.getId()).get();
        
        Assertions.assertThat(orderFound.getId()).isEqualTo(savedOrder2.getId());
    }

    @Test
    public void orderRepository_deleteById_deletesOrder() {
        ArrayList<String> ingredientsTestBurger1 = new ArrayList<>();
        ingredientsTestBurger1.addAll(Arrays.asList("cheddar", "poulet"));
        Burger burger1 = new Burger("testBurger1", ingredientsTestBurger1);
        Burger savedBurger1 = burgerRepository.save(burger1);
        ArrayList<String> ingredientsTestBurger2 = new ArrayList<>();
        ingredientsTestBurger2.addAll(Arrays.asList("cheddar", "salad"));
        Burger burger2 = new Burger("testBurger2", ingredientsTestBurger2);
        Burger savedBurger2 = burgerRepository.save(burger2);
        Order order1 = new Order(Arrays.asList(savedBurger1, savedBurger2));
        Order order2 = new Order(Arrays.asList(savedBurger2));
        orderRepository.save(order1);
        Order savedOrder2 = orderRepository.save(order2);

        orderRepository.deleteById(savedOrder2.getId());
        Optional<Order> orderFound = orderRepository.findById(savedOrder2.getId());
        
        Assertions.assertThat(orderFound).isEmpty();
    }
}
