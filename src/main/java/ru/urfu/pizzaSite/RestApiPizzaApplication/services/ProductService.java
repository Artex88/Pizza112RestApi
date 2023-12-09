package ru.urfu.pizzaSite.RestApiPizzaApplication.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.ShowDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Product;
import ru.urfu.pizzaSite.RestApiPizzaApplication.repositories.ProductRepository;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.enums.ProductTypes;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    private final ProductTypeService productTypeService;
    @Autowired
    public ProductService(ProductRepository productRepository, ProductTypeService productTypeService) {
        this.productRepository = productRepository;
        this.productTypeService = productTypeService;
    }

    public int countAllProductsPerType(ProductTypes productType){
        return productRepository.findAllByProductType(productTypeService.findByName(productType)).size();
    }

    public List<Product> findAllCertainProductsAndDisplayCertainCount(ProductTypes productType , Pageable pageable){
        Page<Product> productPage = productRepository.findByProductType(productTypeService.findByName(productType), pageable);
        return productPage.getContent();
    }

    public Pageable getPageable(ShowDTO showDTO) {
        Pageable pageable;
        if (showDTO.getSort() == null)
            pageable = PageRequest.of(0, showDTO.getCount(), Sort.by("id"));
        else if (showDTO.getSort().contains("-"))
            pageable = PageRequest.of(0, showDTO.getCount(), Sort.by(Sort.Direction.DESC, showDTO.getSort().substring(1)));
        else
            pageable = PageRequest.of(0, showDTO.getCount(), Sort.by(showDTO.getSort()));
        return pageable;
    }
}
