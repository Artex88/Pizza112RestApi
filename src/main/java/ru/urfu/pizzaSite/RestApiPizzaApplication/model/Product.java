package ru.urfu.pizzaSite.RestApiPizzaApplication.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    @NotNull
    @NotEmpty(message = "Название продукта не может быть пустым")
    @Size(max = 128, message = "Название может быть максимум 128 символов")
    private String productName;

    @Column(name = "description")
    @Size(max = 255, message = "Описание может быть максимум 255 символов")
    private String ProductDescription;

    @Column(name = "base_price")
    @NotNull
    @NotEmpty(message = "Цена продукта не может быть пустой")
    private double basePrice;


    @Column(name = "image_name")
    @Size(max = 255, message = "Путь к картике может быть максимум 255 символов")
    private String imageName;

    @ManyToOne()
    @JoinColumn(name = "product_type_name", referencedColumnName = "name")
    private ProductType productType;

    @OneToMany(mappedBy = "product")
    private Set<ProductVariant> productVariants;

    @OneToMany(mappedBy = "product")
    private Set<BucketItem> bucketItemSet;

    public Product() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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


    public String getImageName() {
        return imageName;
    }

    public void setImageName(String photoName) {
        this.imageName = photoName;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }


    public Set<ProductVariant> getProductVariants() {
        return productVariants;
    }

    public void setProductVariants(Set<ProductVariant> productVariants) {
        this.productVariants = productVariants;
    }

    public Set<BucketItem> getBucketItemSet() {
        return bucketItemSet;
    }

    public void setBucketItemSet(Set<BucketItem> bucketItemSet) {
        this.bucketItemSet = bucketItemSet;
    }
}
