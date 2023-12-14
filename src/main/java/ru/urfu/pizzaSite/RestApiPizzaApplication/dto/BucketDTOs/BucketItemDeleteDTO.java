package ru.urfu.pizzaSite.RestApiPizzaApplication.dto.BucketDTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.enums.ProductVariants;
@Schema(description = "Сущность для передачи запроса на удаление 1 единицы продукта из корзины(БД)")
public class BucketItemDeleteDTO {
    @NotNull(message = "Поле id продукта не может быть null")
    @Digits(integer = 5, fraction = 0)
    @Schema(description = "Id идентификатор продукта")
    private Integer productId;

    @NotNull(message = "Поле варианта товара не может быть null")
    @Schema(description = "Варинт продукта, подробнее про варианты смотреть в ProductVariants enum")
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
