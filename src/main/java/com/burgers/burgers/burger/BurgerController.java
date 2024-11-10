package com.burgers.burgers.burger;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class BurgerController {

  private final BurgerRepository repository;

  private final BurgerModelAssembler assembler;

  BurgerController(BurgerRepository repository, BurgerModelAssembler assembler) {
    this.repository = repository;
    this.assembler = assembler;
  }


  // Aggregate root
  // tag::get-aggregate-root[]
  @GetMapping("/burgers")
  CollectionModel<EntityModel<Burger>> all() {
    List<EntityModel<Burger>> burgers = repository.findAll().stream()
      .map(assembler::toModel)
      .collect(Collectors.toList());

    return CollectionModel.of(burgers, linkTo(methodOn(BurgerController.class).all()).withSelfRel());
  }
  // end::get-aggregate-root[]

  @PostMapping("/burgers")
  ResponseEntity<?> createBurger(@RequestBody Burger newBurger) {
    EntityModel<Burger> entityModel = assembler.toModel(repository.save(newBurger));
    return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel);
  }

  // Single item
  
  @GetMapping("/burgers/{id}")
  EntityModel<Burger> one(@PathVariable Long id) {
    
    Burger burger = repository.findById(id)
      .orElseThrow(() -> new BurgerNotFoundException(id));

      return assembler.toModel(burger);
  }

  @PutMapping("/burgers/{id}")
  ResponseEntity<?> replaceBurger(@RequestBody Burger newBurger, @PathVariable Long id) {
    
    Burger replacedBurger = repository.findById(id)
      .map(burger -> {
        burger.setName(newBurger.getName());
        burger.setIngredients(newBurger.getIngredients());
        return repository.save(burger);
      })
      .orElseGet(() -> {
        newBurger.setId(id);
        return repository.save(newBurger);
      });
    EntityModel<Burger> entityModel = assembler.toModel(replacedBurger);
    return ResponseEntity
          .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
          .body(entityModel);
  }

  @PatchMapping("/burgers/{id}")
  ResponseEntity<?> patchBurger(@RequestBody Burger newBurger, @PathVariable Long id) {
    
    Burger updatedBurger = repository.findById(id)
      .map(burger -> {
        if(newBurger.getName() != null) burger.setName(newBurger.getName());
        if(newBurger.getIngredients() != null) burger.setIngredients(newBurger.getIngredients());
        return repository.save(burger);
      })
      .orElseThrow(() -> new BurgerNotFoundException(id));
      EntityModel<Burger> entityModel = assembler.toModel(updatedBurger);
      return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel);
  }

  @DeleteMapping("/burgers/{id}")
  ResponseEntity<?> deleteBurger(@PathVariable Long id) {
    if(repository.existsById(id)){
      try {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
      } catch (DataIntegrityViolationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Cannot delete burger because it is in orders");
      } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the burger");
      }
    } else {
      throw new BurgerNotFoundException(id);
    }
  }
}
