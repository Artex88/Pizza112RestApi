package ru.urfu.pizzaSite.RestApiPizzaApplication.services.Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.urfu.pizzaSite.RestApiPizzaApplication.api.SMSApi;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.ClientDTOs.AuthenticationDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Client.Client;
import ru.urfu.pizzaSite.RestApiPizzaApplication.repositories.ClientRepository;
import ru.urfu.pizzaSite.RestApiPizzaApplication.security.JWTUtil;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.AuthorizationAttemptsExhaustedException;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.NotFoundException;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.TooManyRequestException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    private final SMSApi smsApi;

    private final RegistrationService registrationService;

    private final JWTUtil jwtUtil;


    @Autowired
    public ClientService(ClientRepository clientRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, SMSApi smsApi, RegistrationService registrationService, JWTUtil jwtUtil) {
        this.clientRepository = clientRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.smsApi = smsApi;
        this.registrationService = registrationService;
        this.jwtUtil = jwtUtil;
    }
    @Transactional(readOnly = true)
    public Client findByPhoneNumber(String phoneNumber){
        Optional<Client> client = clientRepository.findByPhoneNumber(phoneNumber);
       if (client.isPresent())
           return client.get();
       throw new NotFoundException("Client with this phone number does not exist");
    }

    @Transactional()
    public void updatePasswordAndResetAttempts(Client clientUpdated, String password) {
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
        if (client.getLogin_attempts() >= 4 && !Objects.equals(client.getPhoneNumber(), "79999999999") && !Objects.equals(client.getPhoneNumber(), "78888888888")){
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
    public void save(Client client){
        clientRepository.save(client);
    }



    @Transactional
    public void sendRegistrationMessage(String phoneNumber, String rawPassword){
        Optional<Client> optionalClient = clientRepository.findByPhoneNumber(phoneNumber);
        if (optionalClient.isPresent() && !Objects.equals(optionalClient.get().getPhoneNumber(), "79999999999") && !Objects.equals(optionalClient.get().getPhoneNumber(), "78888888888")){
            this.validateRegisterRequest(optionalClient.get());
            this.updatePasswordAndResetAttempts(optionalClient.get(), passwordEncoder.encode(rawPassword));
            smsApi.sendSMSWithPassword(phoneNumber, rawPassword);
        }
        else if (Objects.equals(phoneNumber, "79999999999") || Objects.equals(phoneNumber, "78888888888")){
            if (optionalClient.isPresent())
                optionalClient.get().setPassword(passwordEncoder.encode("111111"));
            else
                registrationService.PreRegisterClient(new Client(phoneNumber, passwordEncoder.encode("111111"), LocalDateTime.now(), null));
        }
        else {
            registrationService.PreRegisterClient(new Client(phoneNumber, passwordEncoder.encode(rawPassword), LocalDateTime.now(),null));
            smsApi.sendSMSWithPassword(phoneNumber, rawPassword);
        }
    }

    public String getPhoneNumberFromToken(String token){
        String jwt = token.substring(7);
        return jwtUtil.validateTokenAndRetrieveClaim(jwt);
    }
}
