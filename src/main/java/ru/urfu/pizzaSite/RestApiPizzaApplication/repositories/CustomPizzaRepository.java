package ru.urfu.pizzaSite.RestApiPizzaApplication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Pizza.CustomPizza;
@Repository
public interface CustomPizzaRepository extends JpaRepository<CustomPizza, Integer> {
}
