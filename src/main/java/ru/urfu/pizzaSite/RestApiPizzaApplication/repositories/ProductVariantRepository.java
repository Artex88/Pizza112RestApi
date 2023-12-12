package ru.urfu.pizzaSite.RestApiPizzaApplication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.ProductVariant;
@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Integer> {
}
