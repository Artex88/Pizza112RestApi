package ru.urfu.pizzaSite.RestApiPizzaApplication.services.Pizza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.pizzaSite.RestApiPizzaApplication.repositories.IngredientRepository;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    @Autowired
    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }
}
