package ru.urfu.pizzaSite.RestApiPizzaApplication.dto.BucketDTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;

@Schema(description = "Сущность, в которой приходит 1 ячейка продукта для корзины")
public class BucketShowItemDTO {

    @Schema(description = "bucket id")
    private int bucketId;
    @Schema(description = "id продукта")
    private int productId;
    @Schema(description = "Название продукта")
    private String name;

    @Column(name = "address")
    @NotEmpty
    private String address;

    @Column(name = "pay_type")
    @NotEmpty
    private String payType;
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

    public BucketShowItemDTO(int id, String address, String payType, String name, String productVariant, Integer quantity, Double itemPrice, Double productVariantPrice, String image, int productId) {
        this.bucketId = id;
        this.address = address;
        this.payType = payType;
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

    public int getBucketId() {
        return bucketId;
    }

    public void setBucketId(int bucketId) {
        this.bucketId = bucketId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }
}
