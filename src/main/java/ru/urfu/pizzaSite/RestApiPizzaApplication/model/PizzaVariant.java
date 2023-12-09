package ru.urfu.pizzaSite.RestApiPizzaApplication.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.List;

@Entity
@Table(name = "pizza_variant")
public class PizzaVariant {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    @NotNull
    @NotEmpty(message = "Кодовое название варианта пиццы не может быть пустым")
    @Size(max = 8, message = "Кодовое название варианта пиццы может быть максимум 8 символов")
    private String pizzaVariantName;

    @Column(name = "price")
    @NotNull
    @NotEmpty(message = "Цена варианта пиццы не может быть пустой")
    private double pizzaVariantPrice;

    @Column(name = "weight")
    @NotNull
    @NotEmpty(message = "Вес варианта пиццы не может быть пустой")
    private double pizzaVariantWeight;

    @OneToMany(mappedBy = "pizzaVariant")
    private List<Pizza> pizzaList;

    @ManyToOne()
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    public PizzaVariant() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPizzaVariantName() {
        return pizzaVariantName;
    }

    public void setPizzaVariantName(String pizzaVariantName) {
        this.pizzaVariantName = pizzaVariantName;
    }

    public double getPizzaVariantPrice() {
        return pizzaVariantPrice;
    }

    public void setPizzaVariantPrice(double pizzaVariantPrice) {
        this.pizzaVariantPrice = pizzaVariantPrice;
    }

    public double getPizzaVariantWeight() {
        return pizzaVariantWeight;
    }

    public void setPizzaVariantWeight(double pizzaVariantWeight) {
        this.pizzaVariantWeight = pizzaVariantWeight;
    }

    public List<Pizza> getPizzaList() {
        return pizzaList;
    }

    public void setPizzaList(List<Pizza> pizzaList) {
        this.pizzaList = pizzaList;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}

