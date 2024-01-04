package ru.urfu.pizzaSite.RestApiPizzaApplication.model.Pizza;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ingredient")
public class Ingredient {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "weight")
    private Double weight;

    @Column(name = "price")
    private Double price;

    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PizzaIngredient> pizzaIngredientSet = new HashSet<>();

    public Ingredient() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Set<PizzaIngredient> getPizzaIngredientSet() {
        return pizzaIngredientSet;
    }

    public void setPizzaIngredientSet(Set<PizzaIngredient> pizzaIngredientSet) {
        this.pizzaIngredientSet = pizzaIngredientSet;
    }
}
