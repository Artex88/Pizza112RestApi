package ru.urfu.pizzaSite.RestApiPizzaApplication.util.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.ProductDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.ShowDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Product;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.ProductService;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.enums.ProductTypes;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.CountException;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.SortException;

import java.util.Arrays;

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
        this.countValidation(showDTO.getCount(), showDTO.getFrom());
        if (showDTO.getSort() != null)
            this.sortValidation(showDTO.getSort());
    }
    private void countValidation(int count, ProductTypes form){
        if (count <= 0)
            throw new CountException("The product count cannot be negative");
        else if (count > productService.countAllProductsPerType(form))
            throw new CountException("The count of the product is greater than what is in the database.");
        else if (count > 15)
            throw new CountException("Too many products for 1 request");
    }

    private void sortValidation(String sort){
        String newSort = sort.replace('-',' ');
        if (Arrays.stream(Product.class.getDeclaredFields()).noneMatch(value -> value.getName().equals(newSort))){
            throw new SortException("Sorting by this field is not possible");
        }
    }
}
