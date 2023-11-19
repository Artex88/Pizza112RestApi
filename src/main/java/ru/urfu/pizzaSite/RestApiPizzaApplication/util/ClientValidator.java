package ru.urfu.pizzaSite.RestApiPizzaApplication.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Client;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.ClientService;

@Component
public class ClientValidator implements Validator {

    private final ClientService clientService;
    @Autowired
    public ClientValidator(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Client.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Client client = (Client) target;
        if (clientService.findByPhoneNumber(client.getPhoneNumber()).isPresent())
            errors.rejectValue("phoneNumber","400","Пользователь с таким номером телефона уже существует");
    }
}
