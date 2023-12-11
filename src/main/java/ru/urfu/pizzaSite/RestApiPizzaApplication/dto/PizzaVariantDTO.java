package ru.urfu.pizzaSite.RestApiPizzaApplication.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PizzaVariantDTO {
    @Column(name = "name")
    @NotNull
    @NotEmpty(message = "Кодовое название варианта пиццы не может быть пустым")
    @Size(max = 8, message = "Кодовое название варианта пиццы может быть максимум 8 символов")
    private String name;

    @Column(name = "price")
    @NotNull
    @NotEmpty(message = "Цена варианта пиццы не может быть пустой")
    private double price;

    @Column(name = "weight")
    @NotNull
    @NotEmpty(message = "Вес варианта пиццы не может быть пустой")
    private double weight;

    public PizzaVariantDTO() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

}
