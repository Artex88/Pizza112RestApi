package ru.urfu.pizzaSite.RestApiPizzaApplication.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.ClientInfo;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.ClientInfoService;

@Component
public class ClientValidator implements Validator {

    private final ClientInfoService clientInfoService;
    @Autowired
    public ClientValidator(ClientInfoService clientInfoService) {
        this.clientInfoService = clientInfoService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return ClientInfo.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
//        ClientInfo clientInfo = (ClientInfo) target;
//        if (clientInfoService.findByPhoneNumber(clientInfo.getPhoneNumber()).isPresent())
//            throw new ClientAlreadyExistAuthenticationException();
    }
}
