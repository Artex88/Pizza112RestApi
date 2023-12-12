package ru.urfu.pizzaSite.RestApiPizzaApplication.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.Set;

@Entity
@Table(name = "product_variant")
public class ProductVariant {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    @NotNull
    @NotEmpty(message = "Кодовое название варианта продукта не может быть пустым")
    @Size(max = 8, message = "Кодовое название варианта продукта может быть максимум 8 символов")
    private String productVariantName;

    @Column(name = "price")
    @NotNull
    @NotEmpty(message = "Цена варианта продукта не может быть пустой")
    private double productVariantPrice;

    @Column(name = "weight")
    private double productVariantWeight;

    @Column(name = "portion_in_milliliters")
    private double portionInMilliliters;

    @ManyToOne()
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @OneToMany(mappedBy = "productVariant")
    private Set<BucketItem> bucketItemSet;


    public ProductVariant() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductVariantName() {
        return productVariantName;
    }

    public void setProductVariantName(String pizzaVariantName) {
        this.productVariantName = pizzaVariantName;
    }

    public double getProductVariantPrice() {
        return productVariantPrice;
    }

    public void setProductVariantPrice(double pizzaVariantPrice) {
        this.productVariantPrice = pizzaVariantPrice;
    }

    public double getProductVariantWeight() {
        return productVariantWeight;
    }

    public void setProductVariantWeight(double pizzaVariantWeight) {
        this.productVariantWeight = pizzaVariantWeight;
    }


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getPortionInMilliliters() {
        return portionInMilliliters;
    }

    public void setPortionInMilliliters(double portionInMilliliters) {
        this.portionInMilliliters = portionInMilliliters;
    }

    public Set<BucketItem> getBucketItemSet() {
        return bucketItemSet;
    }

    public void setBucketItemSet(Set<BucketItem> bucketItemSet) {
        this.bucketItemSet = bucketItemSet;
    }
}

