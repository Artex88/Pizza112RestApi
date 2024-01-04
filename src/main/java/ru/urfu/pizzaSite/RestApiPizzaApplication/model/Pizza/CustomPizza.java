package ru.urfu.pizzaSite.RestApiPizzaApplication.model.Pizza;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "custom_pizza")
public class CustomPizza {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "price")
    private Double price;

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "customPizza", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PizzaIngredient> pizzaIngredientSet = new HashSet<>();

    public CustomPizza() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<PizzaIngredient> getPizzaIngredientSet() {
        return pizzaIngredientSet;
    }

    public void setPizzaIngredientSet(Set<PizzaIngredient> pizzaIngredientSet) {
        this.pizzaIngredientSet = pizzaIngredientSet;
    }
}
