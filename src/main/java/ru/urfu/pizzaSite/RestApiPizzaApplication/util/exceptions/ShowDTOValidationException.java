package ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions;

import org.springframework.validation.BindingResult;

public class ShowDTOValidationException extends RuntimeException{
    private final BindingResult bindingResult;

    public ShowDTOValidationException(BindingResult bindingResult){
        this.bindingResult = bindingResult;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }
}
