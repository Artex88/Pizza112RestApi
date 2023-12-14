package ru.urfu.pizzaSite.RestApiPizzaApplication.services.Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Client;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.ClientInfo;
import ru.urfu.pizzaSite.RestApiPizzaApplication.repositories.ClientRepository;
import ru.urfu.pizzaSite.RestApiPizzaApplication.repositories.ClientInfoRepository;

@Service
public class RegistrationService {
    private final ClientInfoRepository clientInfoRepository;

    private final ClientRepository clientRepository;

    @Autowired
    public RegistrationService(ClientInfoRepository clientInfoRepository, ClientRepository clientRepository) {
        this.clientInfoRepository = clientInfoRepository;
        this.clientRepository = clientRepository;
    }

    @Transactional
    public void register(ClientInfo clientInfo, Client client){
        clientInfo.setClient(client);
        clientInfo.setImageName("default.webp");
        clientRepository.save(client);
        clientInfoRepository.save(clientInfo);
    }


    @Transactional
    public void PreRegisterClient(Client client){
        clientRepository.save(client);
    }
}
