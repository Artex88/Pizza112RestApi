package ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions;


import org.springframework.validation.BindingResult;

public class ClientValidationError extends RuntimeException{
    private final BindingResult bindingResult;

    public ClientValidationError(BindingResult bindingResult){
        this.bindingResult = bindingResult;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }
}
