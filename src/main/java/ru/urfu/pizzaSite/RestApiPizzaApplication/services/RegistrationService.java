package ru.urfu.pizzaSite.RestApiPizzaApplication.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.AuthenticationDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Client;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.ClientInfo;
import ru.urfu.pizzaSite.RestApiPizzaApplication.repositories.ClientRepository;
import ru.urfu.pizzaSite.RestApiPizzaApplication.repositories.ClientInfoRepository;

import java.util.Collections;

@Service
public class RegistrationService {
    private final ClientInfoRepository clientInfoRepository;

    private final PasswordEncoder passwordEncoder;



    private final ClientRepository clientRepository;

    @Autowired
    public RegistrationService(ClientInfoRepository clientInfoRepository, PasswordEncoder passwordEncoder, ClientRepository clientRepository) {
        this.clientInfoRepository = clientInfoRepository;
        this.passwordEncoder = passwordEncoder;
        this.clientRepository = clientRepository;
    }

    @Transactional
    public void register(ClientInfo clientInfo, Client client){
        clientInfo.setClient(client);
        clientInfo.setPhotoName("default.png");
        clientRepository.save(client);
        clientInfoRepository.save(clientInfo);
    }


    @Transactional
    public void PreRegisterClient(Client client){
        clientRepository.save(client);
    }
}
