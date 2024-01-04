package ru.urfu.pizzaSite.RestApiPizzaApplication.services.Pizza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.pizzaSite.RestApiPizzaApplication.repositories.CustomPizzaRepository;

@Service
public class CustomPizzaService {

    private final CustomPizzaRepository customPizzaRepository;

    @Autowired
    public CustomPizzaService(CustomPizzaRepository customPizzaRepository) {
        this.customPizzaRepository = customPizzaRepository;
    }
}
