package com.burgers.burgers.burger;

import java.util.ArrayList;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Burger {

  private @Id @GeneratedValue Long id;
  private String name;
  private ArrayList<String> ingredients;

  public Burger() {}

  public Burger(String name, ArrayList<String> ingredients) {
    this.name = name;
    this.ingredients = ingredients;
  }

  public Long getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public ArrayList<String> getIngredients() {
    return this.ingredients;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setIngredients(ArrayList<String> ingredients) {
    this.ingredients = ingredients;
  }

  public void addIngredient(String ingredient) {
    this.ingredients.add(ingredient);
  }

  public void removeIngredient(String ingredient) {
    this.ingredients.remove(ingredient);
  }

  @Override
  public boolean equals(Object o) {

    if (this == o)
      return true;
    if (!(o instanceof Burger))
      return false;
    Burger burger = (Burger) o;
    return Objects.equals(this.id, burger.id) && Objects.equals(this.name, burger.name)
        && this.ingredients.size() == burger.getIngredients().size() && this.ingredients.containsAll(burger.getIngredients());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.name, this.ingredients);
  }

  @Override
  public String toString() {
    return "Burger{" + "id=" + this.id + ", name='" + this.name + '\'' + ", ingredients='" + this.ingredients + '\'' + '}';
  }
}