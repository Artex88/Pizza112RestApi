package ru.urfu.pizzaSite.RestApiPizzaApplication.util.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.BucketItemAddDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.CountException;
@Component
public class BucketItemValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return BucketItemAddDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BucketItemAddDTO bucketItemAddDTO = (BucketItemAddDTO) target;
        if (bucketItemAddDTO.getQuantity() > 15)
            throw new CountException("Too many quantity in 1 BucketItem");
        if (bucketItemAddDTO.getQuantity() <= 0)
            throw new CountException("Quantity must be at least one");
    }
}
