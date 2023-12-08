package ru.urfu.pizzaSite.RestApiPizzaApplication.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.ProductType;
import ru.urfu.pizzaSite.RestApiPizzaApplication.repositories.ProductTypeRepository;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.ProductTypes;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.ProductTypeNotFoundException;

import java.util.Optional;

@Service
public class ProductTypeService {

    private final ProductTypeRepository productTypeRepository;

    @Autowired
    public ProductTypeService(ProductTypeRepository productTypeRepository) {
        this.productTypeRepository = productTypeRepository;
    }

    @Transactional(readOnly = true)
    public ProductType findByName(ProductTypes productType){
        Optional<ProductType> productTypeOptional = productTypeRepository.findByName(productType.name());
        if (productTypeOptional.isPresent())
            return productTypeOptional.get();
        // TODO ДОБАВИТЬ КАСТОМНУЮ ОШИБКУ
        throw new ProductTypeNotFoundException();
    }
}
