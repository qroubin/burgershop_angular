package com.burgers.burgers.burger;

public class BurgerNotFoundException extends RuntimeException {

  public BurgerNotFoundException(Long id) {
    super("Could not find burger " + id);
  }
}