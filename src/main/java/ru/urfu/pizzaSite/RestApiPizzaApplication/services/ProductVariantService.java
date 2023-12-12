package ru.urfu.pizzaSite.RestApiPizzaApplication.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.CartItemAddDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Product;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.ProductVariant;
import ru.urfu.pizzaSite.RestApiPizzaApplication.repositories.ProductVariantRepository;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.NotFoundException;

import javax.management.BadAttributeValueExpException;
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

    public ProductVariant getProductVariantFromProduct(CartItemAddDTO cartItemAddDTO, Product product) {
        Optional<ProductVariant> productVariantOptional = product.getProductVariants().stream().filter(productVariant1 -> Objects.equals(productVariant1.getProductVariantName(), cartItemAddDTO.getProductVariant().name())).findFirst();
        if (productVariantOptional.isEmpty())
            throw new NotFoundException("This product type does not exist for this product");
        return productVariantOptional.get();
    }
}
