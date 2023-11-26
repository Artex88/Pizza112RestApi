package ru.urfu.pizzaSite.RestApiPizzaApplication.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.ClientInfo;
import ru.urfu.pizzaSite.RestApiPizzaApplication.repositories.ClientInfoRepository;

import java.util.Optional;
@Service
public class ClientInfoService {

    private final ClientInfoRepository clientInfoRepository;

    @Autowired
    public ClientInfoService(ClientInfoRepository clientInfoRepository) {
        this.clientInfoRepository = clientInfoRepository;
    }

    @Transactional(readOnly = true)
    public Optional<ClientInfo> findByPhoneNumber(String phoneNumber){
        return clientInfoRepository.findByPhoneNumber(phoneNumber);
    }
    public boolean isClientExist(String phoneNumber){
        return clientInfoRepository.findByPhoneNumber(phoneNumber).isPresent();
    }

}
