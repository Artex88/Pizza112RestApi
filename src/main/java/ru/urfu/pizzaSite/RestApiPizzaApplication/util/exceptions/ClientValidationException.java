package ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions;


import org.springframework.validation.BindingResult;

public class ClientValidationException extends RuntimeException{
    private final BindingResult bindingResult;

    public ClientValidationException(BindingResult bindingResult){
        this.bindingResult = bindingResult;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }
}
