package ru.urfu.pizzaSite.RestApiPizzaApplication.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.ProductDTOs.ShowDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Product;
import ru.urfu.pizzaSite.RestApiPizzaApplication.repositories.ProductRepository;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.enums.ProductTypes;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    private final ProductTypeService productTypeService;
    @Autowired
    public ProductService(ProductRepository productRepository, ProductTypeService productTypeService) {
        this.productRepository = productRepository;
        this.productTypeService = productTypeService;
    }
    @Transactional(readOnly = true)
    public int countAllProductsPerType(ProductTypes productType){
        return productRepository.findAllByProductType(productTypeService.findByName(productType)).size();
    }
    @Transactional(readOnly = true)
    public List<Product> findAllCertainProductsAndDisplayCertainCount(ProductTypes productType , Pageable pageable){
        Page<Product> productPage = productRepository.findByProductType(productTypeService.findByName(productType), pageable);
        return productPage.getContent();
    }

    @Transactional(readOnly = true)
    public Product findById(int id){
        Optional<Product> productOptional= productRepository.findById(id);
        if (productOptional.isEmpty())
            throw new NotFoundException("Product with this id does not exist");
        return productOptional.get();
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
