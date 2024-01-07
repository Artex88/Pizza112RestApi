package ru.urfu.pizzaSite.RestApiPizzaApplication.dto.ProductDTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class DrinkDTO {

    @Column(name = "name")
    @NotNull
    @NotEmpty(message = "Кодовое название варианта пиццы не может быть пустым")
    @Size(max = 8, message = "Кодовое название варианта пиццы может быть максимум 8 символов")
    @Schema(description = """
            Кодовое название напитков. Существует 8 типов:
             SMV: маленький ванильный милкшейк\s
             SMC: маленький шоколадный милкшейк
             SMS: маленький клубничный милкшейк
             MMV: средний ванильный милкшейк
             MMC: средний шоколадный милкшейк
             MMS: средний клубничный милкшейк
             SJA: маленький яблочный сок\s
             SJO: маленький апельсиновый сок
             SJC: маленький вишневый сок
             MJA: средний яблочный сок
             MJO: средний апельсиновый сок
             MJC: средний вишневый сок
             SC: маленький капучино
             MC: средний капучино
             LL: большой латте
             ML: средний латте
             SA: маленький американо
             MA: средний американо
             SCo: маленький какао
             MCo: средний какао"""
    )
    private String name;

    @Column(name = "price")
    @NotNull(message = "Цена пиццы не может быть null")
    @Schema(description = "Цена продукта")
    private Double price;

    @Column(name = "portion_in_milliliters")
    private double portionInMilliliters;

    @Column(name = "image_name")
    @Schema(description = "Картинка продукта(тут передается одна и таже картинка в каждом новом варианте)")
    private String image;

    public DrinkDTO() {
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

    public double getPortionInMilliliters() {
        return portionInMilliliters;
    }

    public void setPortionInMilliliters(double portionInMilliliters) {
        this.portionInMilliliters = portionInMilliliters;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
