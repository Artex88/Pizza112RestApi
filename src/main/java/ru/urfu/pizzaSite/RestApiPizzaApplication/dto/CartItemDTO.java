package ru.urfu.pizzaSite.RestApiPizzaApplication.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.enums.ProductVariants;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.interfaces.EnumValidator;

public class CartItemDTO {
    @NotNull(message = "Поле id продукта не может быть null")
    @NotEmpty(message = "Поле id продукта не может быть пустым")
    private int productId;
    @NotNull(message = "Поле кол-ва выбраного продукта не может быть null")
    @NotEmpty(message = "Поле кол-ва продукта не может быть пустым")
    private int quantity;
    @NotNull(message = "Поле варианта продукта не может быть null")
    @NotEmpty(message = "Поле варианта продукта не может быть пустым")
    @EnumValidator(enumClazz = ProductVariants.class, message = "Неправильно указан тип вида продукта")
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
