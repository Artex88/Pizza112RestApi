package ru.urfu.pizzaSite.RestApiPizzaApplication.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.ClientInfoDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Client;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.ClientInfo;
import ru.urfu.pizzaSite.RestApiPizzaApplication.repositories.ClientInfoRepository;
import ru.urfu.pizzaSite.RestApiPizzaApplication.security.JWTUtil;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.ClientInfoNotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
@Service
public class ClientInfoService {

    private final ClientInfoRepository clientInfoRepository;


    private final JWTUtil jwtUtil;

    @Autowired
    public ClientInfoService(ClientInfoRepository clientInfoRepository, JWTUtil jwtUtil) {
        this.clientInfoRepository = clientInfoRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional(readOnly = true)
    public ClientInfo findByPhoneNumber(String phoneNumber){
        Optional<ClientInfo> clientInfoOptional = clientInfoRepository.findByPhoneNumber(phoneNumber);
        if (clientInfoOptional.isPresent())
            return clientInfoOptional.get();
        throw new ClientInfoNotFoundException();
    }

    @Transactional
    public Client updateClientInfo(Client client, ClientInfoDTO clientInfoDTO){
        ClientInfo clientInfo = client.getClient_info();
        clientInfo.setName(clientInfoDTO.getName());
        clientInfo.setSurname(clientInfoDTO.getSurname());
        clientInfo.setPatronymic(clientInfoDTO.getPatronymic());
        clientInfo.setDateOfBirth(clientInfoDTO.getDateOfBirth());
        clientInfo.setEmail(clientInfoDTO.getEmail());
        return client;
    }

    public Map<String, String> fillClientInfoJSON(ClientInfo clientInfo) {
        Map<String,String> json = new HashMap<>();
        json.put("name", clientInfo.getName());
        json.put("surname", clientInfo.getSurname());
        json.put("patronymic", clientInfo.getPatronymic());
        if (clientInfo.getDateOfBirth() == null)
            json.put("dateOfBirth", null);
        else
            json.put("dateOfBirth", clientInfo.getDateOfBirth().toString());
        json.put("email", clientInfo.getEmail());
        return json;
    }

    @Transactional(readOnly = true)
    public boolean isClientExist(String phoneNumber){
        return clientInfoRepository.findByPhoneNumber(phoneNumber).isPresent();
    }
    @Transactional
    public void save(ClientInfo clientInfo){
        clientInfoRepository.save(clientInfo);
    }

    public String getPhoneNumberFromToken(String token){
        String jwt = token.substring(7);
        return jwtUtil.validateTokenAndRetrieveClaim(jwt);
    }
}
