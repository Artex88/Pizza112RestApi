package ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String message) {
        super(message);
    }
}
