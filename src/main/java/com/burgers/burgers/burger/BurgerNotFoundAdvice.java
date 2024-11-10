package com.burgers.burgers.burger;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class BurgerNotFoundAdvice {

  @ResponseBody
  @ExceptionHandler(BurgerNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String burgerNotFoundHandler(BurgerNotFoundException ex) {
    return ex.getMessage();
  }
}