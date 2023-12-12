package ru.urfu.pizzaSite.RestApiPizzaApplication.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.ProductVariant;
import ru.urfu.pizzaSite.RestApiPizzaApplication.repositories.ProductVariantRepository;

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
}
