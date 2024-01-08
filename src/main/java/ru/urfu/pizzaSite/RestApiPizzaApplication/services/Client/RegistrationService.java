package ru.urfu.pizzaSite.RestApiPizzaApplication.services.Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Client.Client;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Client.ClientInfo;
import ru.urfu.pizzaSite.RestApiPizzaApplication.repositories.ClientRepository;
import ru.urfu.pizzaSite.RestApiPizzaApplication.repositories.ClientInfoRepository;
import ru.urfu.pizzaSite.RestApiPizzaApplication.security.TelegramTokenGenerator;

@Service
public class RegistrationService {
    private final ClientInfoRepository clientInfoRepository;

    private final TelegramTokenGenerator telegramTokenGenerator;

    private final ClientRepository clientRepository;

    @Autowired
    public RegistrationService(ClientInfoRepository clientInfoRepository, TelegramTokenGenerator telegramTokenGenerator, ClientRepository clientRepository) {
        this.clientInfoRepository = clientInfoRepository;
        this.telegramTokenGenerator = telegramTokenGenerator;
        this.clientRepository = clientRepository;
    }

    @Transactional
    public void register(ClientInfo clientInfo, Client client){
        clientInfo.setClient(client);
        clientInfo.setImageName("default.webp");
        clientInfo.setTgToken(telegramTokenGenerator.generateNewToken());
        clientRepository.save(client);
        clientInfoRepository.save(clientInfo);
    }


    @Transactional
    public void PreRegisterClient(Client client){
        clientRepository.save(client);
    }
}
