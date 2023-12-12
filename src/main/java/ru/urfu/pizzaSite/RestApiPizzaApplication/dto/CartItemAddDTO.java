package ru.urfu.pizzaSite.RestApiPizzaApplication.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.enums.ProductVariants;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.interfaces.EnumValidator;

public class CartItemAddDTO {
    @NotNull(message = "Поле id продукта не может быть null")
    @Digits(integer = 5, fraction = 0)
    private Integer productId;
    @NotNull(message = "Поле кол-ва выбраного продукта не может быть null")
    @Digits(integer = 2, fraction = 0)
    private Integer quantity;
    @NotNull(message = "Поле варианта продукта не может быть null")
    private ProductVariants productVariant;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ProductVariants getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariants productVariant) {
        this.productVariant = productVariant;
    }
}
