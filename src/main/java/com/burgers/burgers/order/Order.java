package com.burgers.burgers.order;

import java.util.List;
import java.util.Objects;

import com.burgers.burgers.burger.Burger;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "ORDERS")
public class Order {
    private @Id @GeneratedValue Long id;
    @ManyToMany
    @JoinTable(
        name = "orders_burger",
        joinColumns = @JoinColumn(name = "order_id"),
        inverseJoinColumns = @JoinColumn(name = "burger_id")
    )
    private List<Burger> burgers;
    private Boolean isProcessed;

    Order() {}

    Order(List<Burger> burgers) {
        this.burgers = burgers;
        this.isProcessed = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Burger> getBurgers() {
        return burgers;
    }

    public void setBurgers(List<Burger> burgers) {
        this.burgers = burgers;
    }
    
    public Boolean getIsProcessed() {
        return isProcessed;
    }

    public void setIsProcessed(Boolean isProcessed) {
        this.isProcessed = isProcessed;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
        return true;
        if (!(o instanceof Order))
        return false;
        Order order = (Order) o;
        return Objects.equals(this.id, order.id) && this.burgers.size() == order.getBurgers().size()
        && this.burgers.containsAll(order.getBurgers()) && this.isProcessed == order.getIsProcessed();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.burgers, this.isProcessed);
    }

    @Override
    public String toString() {
        return "Order{" + "id=" + this.id + ", burgers='" + this.burgers + '\'' + ", isProcessed='" + this.isProcessed + '\'' + '}';
    }
}
