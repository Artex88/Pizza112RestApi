package ru.urfu.pizzaSite.RestApiPizzaApplication.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Product.Product;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Product.ProductType;


import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findAllByProductType(ProductType productType);
    Page<Product> findByProductType(ProductType productType, Pageable pageable);
    Optional<Product> findById (int id);
}
