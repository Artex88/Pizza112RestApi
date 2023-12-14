package ru.urfu.pizzaSite.RestApiPizzaApplication.dto.BucketDTOs;

public class BucketShowItemDTO {

    private String name;

    private String variant;

    private Integer quantity;

    private Double itemPrice;

    private Double productVariantPrice;

    private String image;

    public BucketShowItemDTO(String name, String variant, Integer quantity, Double itemPrice, Double productVariantPrice, String image) {
        this.name = name;
        this.variant = variant;
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

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
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
}
