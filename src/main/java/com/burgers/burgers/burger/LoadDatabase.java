package com.burgers.burgers.burger;

import java.util.ArrayList;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

  private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

  @Bean
  CommandLineRunner initDatabase(BurgerRepository repository) {

    ArrayList<String> ingredientsCheeseburger = new ArrayList<>();
    ingredientsCheeseburger.addAll(Arrays.asList("cheddar", "steak", "salade", "oignons"));

    ArrayList<String> ingredientsChickenburger = new ArrayList<>();
    ingredientsChickenburger.addAll(Arrays.asList("cheddar", "poulet", "oignons"));

    return args -> {
      log.info("Preloading " + repository.save(new Burger("Cheeseburger", ingredientsCheeseburger)));
      log.info("Preloading " + repository.save(new Burger("Chickenburger", ingredientsChickenburger)));
    };
  }
}