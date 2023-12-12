package ru.urfu.pizzaSite.RestApiPizzaApplication.util.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.CartItemDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.CountException;
@Component
public class CartItemValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return CartItemDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CartItemDTO cartItemDTO = (CartItemDTO) target;
        if (cartItemDTO.getQuantity() > 15)
            throw new CountException("Too many quantity in 1 cartItem");
    }
}
