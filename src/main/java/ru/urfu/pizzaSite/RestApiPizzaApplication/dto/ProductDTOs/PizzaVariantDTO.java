package ru.urfu.pizzaSite.RestApiPizzaApplication.dto.ProductDTOs;

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
    @NotNull(message = "Цена пиццы не может быть null")
    private Double price;

    @Column(name = "weight")
    @NotNull(message = "Вес пиццы не может быть null")
    private Double weight;
    @Column(name = "image_name")
    private String image;

    public PizzaVariantDTO() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
