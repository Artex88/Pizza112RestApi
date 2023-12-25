package ru.urfu.pizzaSite.RestApiPizzaApplication.dto.BucketDTOs;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Сущность, в которой приходит 1 ячейка продукта для корзины")
public class BucketShowItemDTO {

    @Schema(description = "bucket id")
    private int id;
    @Schema(description = "id продукта")
    private int productId;
    @Schema(description = "Название продукта")
    private String name;
    @Schema(description = "Варинт продукта, подробнее про варианты смотреть в ProductVariants enum")
    private String productVariant;
    @Schema(description = "количество единиц продукта в ячейке")
    private Integer quantity;
    @Schema(description = "общая цена продукта в ячейке(количество умноженное на базовую цену)")
    private Double itemPrice;
    @Schema(description = "цена варианта продукта в единичном экземпляре")
    private Double productVariantPrice;
    @Schema(description = "идентификатор картинки продукта")
    private String image;

    public BucketShowItemDTO(int id, String name, String productVariant, Integer quantity, Double itemPrice, Double productVariantPrice, String image, int productId) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.productVariant = productVariant;
        this.quantity = quantity;
        this.itemPrice = itemPrice;
        this.productVariantPrice = productVariantPrice;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(String productVariant) {
        this.productVariant = productVariant;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Double getProductVariantPrice() {
        return productVariantPrice;
    }

    public void setProductVariantPrice(Double productVariantPrice) {
        this.productVariantPrice = productVariantPrice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
