package ru.urfu.pizzaSite.RestApiPizzaApplication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Pizza.Ingredient;
@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
}
