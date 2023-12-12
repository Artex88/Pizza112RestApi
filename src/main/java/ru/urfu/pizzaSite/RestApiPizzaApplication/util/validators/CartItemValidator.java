package ru.urfu.pizzaSite.RestApiPizzaApplication.util.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.CartItemAddDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.CountException;
@Component
public class CartItemValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return CartItemAddDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CartItemAddDTO cartItemAddDTO = (CartItemAddDTO) target;
        if (cartItemAddDTO.getQuantity() > 15)
            throw new CountException("Too many quantity in 1 cartItem");
        if (cartItemAddDTO.getQuantity() <= 0)
            throw new CountException("Quantity must be at least one");
    }
}
