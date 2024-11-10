package com.burgers.burgers.burger;

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

@SpringBootTest
//@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Transactional      // this reverts changes after each test
public class BurgerRepositoryTests {
    
    @Autowired
    private BurgerRepository burgerRepository;

    @BeforeEach
    public void setup() {
        burgerRepository.deleteAll(); // Delete all data, including burgers from the LoadDatabase class
    }

    @Test
    public void burgerRepository_deleteAll_deletesAllBurgers() {
        ArrayList<String> ingredientsTestBurger1 = new ArrayList<>();
        ingredientsTestBurger1.addAll(Arrays.asList("cheddar", "poulet"));
        Burger burger1 = new Burger("testBurger1", ingredientsTestBurger1);
        ArrayList<String> ingredientsTestBurger2 = new ArrayList<>();
        ingredientsTestBurger2.addAll(Arrays.asList("cheddar", "salad"));
        Burger burger2 = new Burger("testBurger2", ingredientsTestBurger2);
        burgerRepository.save(burger1);
        burgerRepository.save(burger2);

        burgerRepository.deleteAll();
        List<Burger> burgersFound = burgerRepository.findAll();
        
        Assertions.assertThat(burgersFound.size()).isEqualTo(0);
    }

    @Test
    public void burgerRepository_save_returnSavedBurger() {
        ArrayList<String> ingredientsTestBurger = new ArrayList<>();
        ingredientsTestBurger.addAll(Arrays.asList("cheddar", "steak"));
        Burger burger = new Burger("testBurger", ingredientsTestBurger);

        Burger savedBurger = burgerRepository.save(burger);

        Assertions.assertThat(savedBurger).isNotNull();
        Assertions.assertThat(savedBurger.getId()).isGreaterThan(0);
    }

    @Test
    public void burgerRepository_findAllById_returnSearchedBurgers() {
        ArrayList<String> ingredientsTestBurger1 = new ArrayList<>();
        ingredientsTestBurger1.addAll(Arrays.asList("cheddar", "poulet"));
        Burger burger1 = new Burger("testBurger1", ingredientsTestBurger1);
        ArrayList<String> ingredientsTestBurger2 = new ArrayList<>();
        ingredientsTestBurger2.addAll(Arrays.asList("cheddar", "salad"));
        Burger burger2 = new Burger("testBurger2", ingredientsTestBurger2);
        ArrayList<String> ingredientsTestBurger3 = new ArrayList<>();
        ingredientsTestBurger3.addAll(Arrays.asList("cheddar", "steak", "oignons"));
        Burger burger3 = new Burger("testBurger3", ingredientsTestBurger3);
        Burger savedBurger1 = burgerRepository.save(burger1);
        burgerRepository.save(burger2);
        Burger savedBurger3 = burgerRepository.save(burger3);

        List<Burger> burgersFound = burgerRepository.findAllById(Arrays.asList(savedBurger1.getId(), savedBurger3.getId()));
        
        Assertions.assertThat(burgersFound.size()).isEqualTo(2);
        Assertions.assertThat(burgersFound.get(0).getId()).isEqualTo(savedBurger1.getId());
        Assertions.assertThat(burgersFound.get(1).getId()).isEqualTo(savedBurger3.getId());
    }

    @Test
    public void burgerRepository_findAll_returnSearchedBurgers() {
        ArrayList<String> ingredientsTestBurger1 = new ArrayList<>();
        ingredientsTestBurger1.addAll(Arrays.asList("cheddar", "poulet"));
        Burger burger1 = new Burger("testBurger1", ingredientsTestBurger1);
        ArrayList<String> ingredientsTestBurger2 = new ArrayList<>();
        ingredientsTestBurger2.addAll(Arrays.asList("cheddar", "steak", "oignons"));
        Burger burger2 = new Burger("testBurger2", ingredientsTestBurger2);
        Burger savedBurger1 = burgerRepository.save(burger1);
        Burger savedBurger2 = burgerRepository.save(burger2);

        List<Burger> burgersFound = burgerRepository.findAll();

        Assertions.assertThat(burgersFound.size()).isEqualTo(2);
        Assertions.assertThat(burgersFound.get(0).getId()).isEqualTo(savedBurger1.getId());
        Assertions.assertThat(burgersFound.get(1).getId()).isEqualTo(savedBurger2.getId());
    }

    @Test
    public void burgerRepository_existsById_returnTrueForRightId() {
        ArrayList<String> ingredientsTestBurger = new ArrayList<>();
        ingredientsTestBurger.addAll(Arrays.asList("cheddar", "poulet"));
        Burger burger = new Burger("testBurger", ingredientsTestBurger);
        Burger savedBurger = burgerRepository.save(burger);

        Boolean isBurgerFound = burgerRepository.existsById(savedBurger.getId());

        Assertions.assertThat(isBurgerFound).isTrue();
    }

    @Test
    public void burgerRepository_existsById_returnFalseForWrongId() {
        ArrayList<String> ingredientsTestBurger = new ArrayList<>();
        ingredientsTestBurger.addAll(Arrays.asList("cheddar", "poulet"));
        Burger burger = new Burger("testBurger", ingredientsTestBurger);
        burgerRepository.save(burger);

        Boolean isBurgerFound = burgerRepository.existsById(99l);

        Assertions.assertThat(isBurgerFound).isFalse();
    }

    @Test
    public void burgerRepository_findById_returnSearchedBurger() {
        ArrayList<String> ingredientsTestBurger1 = new ArrayList<>();
        ingredientsTestBurger1.addAll(Arrays.asList("cheddar", "poulet"));
        Burger burger1 = new Burger("testBurger1", ingredientsTestBurger1);
        ArrayList<String> ingredientsTestBurger2 = new ArrayList<>();
        ingredientsTestBurger2.addAll(Arrays.asList("cheddar", "salad"));
        Burger burger2 = new Burger("testBurger2", ingredientsTestBurger2);
        burgerRepository.save(burger1);
        Burger savedBurger2 = burgerRepository.save(burger2);

        Burger burgerFound = burgerRepository.findById(savedBurger2.getId()).get();
        
        Assertions.assertThat(burgerFound.getId()).isEqualTo(savedBurger2.getId());
    }

    @Test
    public void burgerRepository_deleteById_deletesBurger() {
        ArrayList<String> ingredientsTestBurger1 = new ArrayList<>();
        ingredientsTestBurger1.addAll(Arrays.asList("cheddar", "poulet"));
        Burger burger1 = new Burger("testBurger1", ingredientsTestBurger1);
        ArrayList<String> ingredientsTestBurger2 = new ArrayList<>();
        ingredientsTestBurger2.addAll(Arrays.asList("cheddar", "salad"));
        Burger burger2 = new Burger("testBurger2", ingredientsTestBurger2);
        burgerRepository.save(burger1);
        Burger savedBurger2 = burgerRepository.save(burger2);

        burgerRepository.deleteById(savedBurger2.getId());
        Optional<Burger> burgerFound = burgerRepository.findById(savedBurger2.getId());
        
        Assertions.assertThat(burgerFound).isEmpty();
    }

}