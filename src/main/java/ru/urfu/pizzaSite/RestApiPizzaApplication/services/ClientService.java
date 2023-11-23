package ru.urfu.pizzaSite.RestApiPizzaApplication.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.AuthenticationDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Client;
import ru.urfu.pizzaSite.RestApiPizzaApplication.repositories.ClientRepository;
import ru.urfu.pizzaSite.RestApiPizzaApplication.security.LongGenerator;
import ru.urfu.pizzaSite.RestApiPizzaApplication.security.TOTPGenerator;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.AuthorizationAttemptsExhaustedException;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.TooManyRequestException;

import java.security.InvalidKeyException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    private final AuthenticationManager authenticationManager;

    @Autowired
    public ClientService(ClientRepository clientRepository, AuthenticationManager authenticationManager) {
        this.clientRepository = clientRepository;
        this.authenticationManager = authenticationManager;
    }
    @Transactional(readOnly = true)
    public Client findByPhoneNumber(String phoneNumber){
        // TODO поменять на кастомную ошибку
        Optional<Client> client = clientRepository.findByPhoneNumber(phoneNumber);
       if (client.isPresent())
           return client.get();
       throw new UsernameNotFoundException("Client not found");
    }

    @Transactional()
    public void updatePasswordAndAttempts(Client clientUpdated, String password) {
        clientUpdated.setLogin_attempts(0);
        clientUpdated.setPassword(password);
        clientRepository.save(clientUpdated);
    }

    @Transactional
    public void validateRegisterRequest(Client client){
        LocalDateTime currenTime = LocalDateTime.now();
        if (currenTime.minusMinutes(1).isAfter(client.getLastRegisterTime())){
            client.setLastRegisterTime(currenTime);
        }else {
            throw new TooManyRequestException();
        }
    }

    @Transactional
    public void validateLoginRequest(Client client,String password){
        LocalDateTime currenTime = LocalDateTime.now();
        if (client.getLogin_attempts() >= 3 ){
            client.setLastLoginTime(currenTime);
            client.setLogin_attempts(0);
            client.setPassword(password);
            clientRepository.save(client);
            throw new AuthorizationAttemptsExhaustedException();
        }
    }


    public void authenticate(AuthenticationDTO authenticationDTO , Client client, String newPassword){
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getPhoneNumber(), authenticationDTO.getPassword(), Collections.emptyList());
        try{
            authenticationManager.authenticate(authenticationToken);
        }catch (BadCredentialsException ignored)
        {
            client.setLogin_attempts(client.getLogin_attempts() + 1);
            clientRepository.save(client);
            throw new BadCredentialsException("");
        }
        client.setLogin_attempts(0);
        client.setPassword(newPassword);
        clientRepository.save(client);
    }



    @Transactional
    public boolean isAuthenticationExist(String phoneNumber){
        return clientRepository.findByPhoneNumber(phoneNumber).isPresent();
    }
}
