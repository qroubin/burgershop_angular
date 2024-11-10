package com.burgers.burgers.burger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
class BurgerModelAssembler implements RepresentationModelAssembler<Burger, EntityModel<Burger>> {

  @Override
  public EntityModel<Burger> toModel(Burger burger) {

    return EntityModel.of(burger, //
        linkTo(methodOn(BurgerController.class).one(burger.getId())).withSelfRel(),
        linkTo(methodOn(BurgerController.class).all()).withRel("burgers"));
  }
}