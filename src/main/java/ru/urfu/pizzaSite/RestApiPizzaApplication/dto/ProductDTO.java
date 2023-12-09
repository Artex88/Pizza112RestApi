package ru.urfu.pizzaSite.RestApiPizzaApplication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
@Schema(description = "Сущность, которая содержит в себе информацию о продукте")
public class ProductDTO {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "id продукта")
    private int id;

    @Column(name = "name")
    @NotNull
    @NotEmpty(message = "Название продукта не может быть пустым")
    @Size(max = 128, message = "Название может быть максимум 128 символов")
    @Schema(description = "поле названия продукта")
    private String productName;

    @Column(name = "description")
    @Size(max = 255, message = "Описание может быть максимум 255 символов")
    @Schema(description = "поле описания продукта(ингредиенты)")
    private String ProductDescription;

    @Column(name = "base_price")
    @NotNull
    @NotEmpty(message = "Цена продукта не может быть пустой")
    @Digits(integer = 4, fraction = 2, message = "Цена может быть максимум 4-х значным, с 2-мя цифрами после запятой")
    @Schema(description = "поле базовой цены продукта")
    private double basePrice;

    @Column(name = "calories")
    @NotNull
    @NotEmpty(message = "Калории продукта не могут быть пустыми")
    @Digits(integer = 3, fraction = 2, message = "Цена может быть максимум 3-х значным, с 2-мя цифрами после запятой")
    @Schema(description = "поле кол-ва каллорий в 100г/мл продукта")
    private double calories;

    @Column(name = "image_name")
    @Size(max = 255, message = "Путь к картике может быть максимум 255 символов")
    @Schema(description = "поле картинки продукта")
    private String imageName;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return ProductDescription;
    }

    public void setProductDescription(String productDescription) {
        ProductDescription = productDescription;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
