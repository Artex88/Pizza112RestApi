package ru.urfu.pizzaSite.RestApiPizzaApplication.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.ShowDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.ProductService;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.TooManyCountException;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.WrongCountException;

@Component
public class ShowDTOValidator implements Validator {
    private final ProductService productService;

    @Autowired
    public ShowDTOValidator(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return ShowDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ShowDTO showDTO = (ShowDTO) target;
        if (showDTO.getCount() > productService.countAllProductsPerType(showDTO.getFrom()) || showDTO.getCount() <= 0)
            throw new WrongCountException();
        else if (showDTO.getCount() > 15)
            throw new TooManyCountException();
    }
}
