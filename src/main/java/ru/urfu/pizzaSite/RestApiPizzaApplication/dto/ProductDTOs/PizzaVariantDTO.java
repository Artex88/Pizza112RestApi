package ru.urfu.pizzaSite.RestApiPizzaApplication.dto.ProductDTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
@Schema(description = "Сущность, которая приходит, когда пользовател нажимает на пиццу, с целью добавить её в корзину(приходит список этих сущностей)")
public class PizzaVariantDTO {
    @Column(name = "name")
    @NotNull
    @NotEmpty(message = "Кодовое название варианта пиццы не может быть пустым")
    @Size(max = 8, message = "Кодовое название варианта пиццы может быть максимум 8 символов")
    @Schema(description = "Кодовое название пиццы. Существует 6 типов:" +
            "\n SP: маленькая пицца, традиционное тесто" +
            "\n STP: маленькая пицца, тонкое тесто" +
            "\n MP: средняя пицца, традиционное тесто" +
            "\n MTP: средняя пицца, тонкое тесто" +
            "\n LP: большая пицца, традиционное тесто" +
            "\n LTP: большая пицца, тонкое тесто"
    )
    private String name;

    @Column(name = "price")
    @NotNull(message = "Цена пиццы не может быть null")
    @Schema(description = "Цена конкретно этого типа пиццы")
    private Double price;

    @Column(name = "weight")
    @NotNull(message = "Вес пиццы не может быть null")
    @Schema(description = "Вес конкретного этого типа пиццы")
    private Double weight;
    @Column(name = "image_name")
    @Schema(description = "Картинка пиццы(тут передается одна и таже картинка в каждом новом варианте)")
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
