package ru.urfu.pizzaSite.RestApiPizzaApplication.dto.BucketDTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.enums.ProductVariants;
@Schema(description = "Сущность для передачи запроса на добавления n-ого количества единиц продукта в корзину(БД)")
public class BucketItemAddDTO {

    @NotNull(message = "Поле id продукта не может быть null")
    @Digits(integer = 5, fraction = 0)
    @Schema(description = "Id идентификатор продукта")
    private Integer productId;
    @NotNull(message = "Поле кол-ва продукта не может быть null")
    @Schema(description = "поле для передачи количества добовляемого продукта")
    private Integer quantity;

    @NotNull(message = "Поле варианта товара не может быть null")
    @Schema(description = "Варинт продукта, подробнее про варианты смотреть в ProductVariants enum")
    private ProductVariants productVariant;

    private boolean isReturnBucketItem;

    public boolean isReturnBucketItem() {
        return isReturnBucketItem;
    }

    public void setReturnBucketItem(boolean returnBucketItem) {
        isReturnBucketItem = returnBucketItem;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ProductVariants getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariants productVariant) {
        this.productVariant = productVariant;
    }
}
