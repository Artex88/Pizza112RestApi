package ru.urfu.pizzaSite.RestApiPizzaApplication.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Client;
import ru.urfu.pizzaSite.RestApiPizzaApplication.repositories.ClientRepository;

import java.util.Optional;
@Service
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Client> findByPhoneNumber(String phoneNumber){
            Optional<Client> client = clientRepository.findByPhoneNumber(phoneNumber);
            return client;
    }
}
