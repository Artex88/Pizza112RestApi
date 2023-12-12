package ru.urfu.pizzaSite.RestApiPizzaApplication.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.BucketItemDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Product;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.ProductVariant;
import ru.urfu.pizzaSite.RestApiPizzaApplication.repositories.ProductVariantRepository;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.enums.ProductVariants;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.NotFoundException;

import java.util.Objects;
import java.util.Optional;

@Service
public class ProductVariantService {

    private final ProductVariantRepository productVariantRepository;

    @Autowired
    public ProductVariantService(ProductVariantRepository productVariantRepository) {
        this.productVariantRepository = productVariantRepository;
    }

    public void save(ProductVariant productVariant){
        productVariantRepository.save(productVariant);
    }

    public ProductVariant getProductVariantFromProduct(BucketItemDTO bucketItemDTO, Product product) {
        return getProductVariant(product, bucketItemDTO.getProductVariant());
    }



    private ProductVariant getProductVariant(Product product, ProductVariants productVariant) {
        if (Objects.equals(productVariant, null))
            throw new NullPointerException();
        Optional<ProductVariant> productVariantOptional = product.getProductVariants().stream().filter(productVariant1 -> Objects.equals(productVariant1.getProductVariantName(), productVariant.name())).findFirst();
        if (productVariantOptional.isEmpty())
            throw new NotFoundException("This product type does not exist for this product");
        return productVariantOptional.get();
    }
}
