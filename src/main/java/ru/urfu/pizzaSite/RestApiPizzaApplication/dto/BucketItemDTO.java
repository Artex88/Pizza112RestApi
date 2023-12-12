package ru.urfu.pizzaSite.RestApiPizzaApplication.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.enums.ProductVariants;

public class BucketItemDTO {
    @NotNull(message = "Поле id продукта не может быть null")
    @Digits(integer = 5, fraction = 0)
    private Integer productId;

    @NotNull(message = "Поле варианта товара не может быть null")
    private ProductVariants productVariant;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public ProductVariants getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariants productVariant) {
        this.productVariant = productVariant;
    }
}
