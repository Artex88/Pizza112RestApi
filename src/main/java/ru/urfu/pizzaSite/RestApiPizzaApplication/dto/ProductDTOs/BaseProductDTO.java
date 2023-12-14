package ru.urfu.pizzaSite.RestApiPizzaApplication.dto.ProductDTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
@Schema(description = "Сущность, которая приходит, когда пользовател нажимает на продукт, у которого нет вариантов, с целью добавить его в корзину.")
public class BaseProductDTO {

    @Column(name = "name")
    @NotNull
    @NotEmpty(message = "Кодовое название варианта пиццы не может быть пустым")
    @Size(max = 8, message = "Кодовое название варианта пиццы может быть максимум 8 символов")
    @Schema(description = "Кодовое название продукта, у которого нету типа: B(английская от слова 'Base')")
    private String name;

    @Column(name = "price")
    @NotNull(message = "Цена пиццы не может быть null")
    @Schema(description = "Цена продукта")
    private Double price;

    @Column(name = "image_name")
    @Schema(description = "Картинка продукта(тут передается одна и таже картинка в каждом новом варианте)")
    private String image;

    public BaseProductDTO(){

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
