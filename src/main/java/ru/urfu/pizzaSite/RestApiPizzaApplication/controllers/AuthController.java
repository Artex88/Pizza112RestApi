package ru.urfu.pizzaSite.RestApiPizzaApplication.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.urfu.pizzaSite.RestApiPizzaApplication.api.SMSApi;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.AuthenticationDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.ClientDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Client;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.ClientInfo;
import ru.urfu.pizzaSite.RestApiPizzaApplication.security.LongGenerator;
import ru.urfu.pizzaSite.RestApiPizzaApplication.security.TOTPGenerator;
import ru.urfu.pizzaSite.RestApiPizzaApplication.security.JWTUtil;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.ClientService;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.ClientInfoService;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.RegistrationService;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.ClientResponse;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.ClientValidationError;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.ClientValidator;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.ClientAlreadyExistAuthenticationException;

import java.security.InvalidKeyException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final RegistrationService registrationService;
    private final ClientValidator clientValidator;

    private final LongGenerator longGenerator;
    private final JWTUtil jwtUtil;

    private final ClientService clientService;

    private final ClientInfoService clientInfoService;
    private final SMSApi smsApi;

    private final TOTPGenerator TOTPGenerator;

    private final ModelMapper modelMapper;



    @Autowired
    public AuthController(RegistrationService registrationService, ClientValidator clientValidator, LongGenerator longGenerator, JWTUtil jwtUtil, ClientService clientService, ClientInfoService clientInfoService, SMSApi smsApi, TOTPGenerator TOTPGenerator, ModelMapper modelMapper) {
        this.registrationService = registrationService;
        this.clientValidator = clientValidator;
        this.longGenerator = longGenerator;
        this.jwtUtil = jwtUtil;
        this.clientService = clientService;
        this.clientInfoService = clientInfoService;
        this.smsApi = smsApi;
        this.TOTPGenerator = TOTPGenerator;
        this.modelMapper = modelMapper;
    }
    @PostMapping("/sms_authentications")
    public ResponseEntity<ClientResponse> performRegistration(@RequestBody @Valid ClientDTO clientDTO, BindingResult bindingResult) throws InvalidKeyException {
            if (bindingResult.hasErrors()){
                throw new ClientValidationError(bindingResult);
            }
            if (clientService.isAuthenticationExist(clientDTO.getPhoneNumber())){
                // Если существует

                Client client = clientService.findByPhoneNumber(clientDTO.getPhoneNumber());
                if (clientService.canMakeRegisterRequest(client)){
                    String hotpPassword = client.getPassword();
                    smsApi.sendSMSWithPassword(clientDTO.getPhoneNumber(), hotpPassword);
                    clientService.startPasswordReplacementCounter(client, LocalDateTime.now(), TOTPGenerator.generatePassword(longGenerator.generateLong()));
                }
                else
                    //TODO МОЖНО ПЕРЕДЕЛАТЬ НА EXCEPTION HANDLER В canMakeRequest КИДАТЬ ИСКЛЮЧЕНИЕ ЕСЛИ FALSE; ДА И ВООБЩЕ ВЕСЬ КОД ВЫШЕ МОЖНО ЗАСУНУТЬ В СЕРВИС В МЕТОД SendMessage или типо того
                    return new ResponseEntity<>(new ClientResponse("Too many request", System.currentTimeMillis()), HttpStatus.TOO_MANY_REQUESTS);
                }
            else {
                // Eсли нет
                String hotpPassword = TOTPGenerator.generatePassword(longGenerator.generateLong());
                registrationService.PreRegisterClient(new Client(clientDTO.getPhoneNumber(), hotpPassword, LocalDateTime.now(),null));
                smsApi.sendSMSWithPassword(clientDTO.getPhoneNumber(), hotpPassword);
                clientService.startPasswordReplacementCounter(clientService.findByPhoneNumber(clientDTO.getPhoneNumber()), LocalDateTime.now(), TOTPGenerator.generatePassword(longGenerator.generateLong()));
            }

            return new ResponseEntity<>(new ClientResponse("Message send", System.currentTimeMillis()), HttpStatus.OK);
    }
    @PostMapping("/sms_check")
    public Map<String, String> performLogin(@RequestBody AuthenticationDTO authenticationDTO) throws InvalidKeyException {
        // TODO ПОЙМАТЬ ОШИБКУ, ЧТО ПОЛЬЗОВАЬЕЛЬ УЖЕ АВТОРИЗОВАН
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){
            Map.of("message", "пользователь уже авторизован");
        }
        Client client = clientService.findByPhoneNumber(authenticationDTO.getPhoneNumber());
        if (clientService.canMakeLoginRequest(client, TOTPGenerator.generatePassword(longGenerator.generateLong()))) {
                if (clientInfoService.isClientExist(authenticationDTO.getPhoneNumber())) {
                    clientService.authenticate(authenticationDTO,client,  TOTPGenerator.generatePassword(longGenerator.generateLong()));
                } else {
                    ClientInfo newClientInfo = convertToClient(authenticationDTO);
                    registrationService.register(newClientInfo, client);
                    clientService.authenticate(authenticationDTO,client, TOTPGenerator.generatePassword(longGenerator.generateLong()));

                }

                String token = jwtUtil.generateToken(authenticationDTO.getPhoneNumber());
                return Map.of("jwt-token", token);
            }
        return Map.of("message", "много запросов");
    }

    @PostMapping("/sms_check/resend")
    public Map<String, String> resendLogin(@RequestBody ClientDTO clientDTO) throws InvalidKeyException{
        Client client = clientService.findByPhoneNumber(clientDTO.getPhoneNumber());
        if (clientService.canMakeRegisterRequest(client)) {
            String hotpPassword = TOTPGenerator.generatePassword(longGenerator.generateLong());
            clientService.updatePasswordAndAttempts(client, hotpPassword);
            smsApi.sendSMSWithPassword(clientDTO.getPhoneNumber(), hotpPassword);
            clientService.startPasswordReplacementCounter(client, LocalDateTime.now(), TOTPGenerator.generatePassword(longGenerator.generateLong()));
            return Map.of("message", "сообщение отправленно");
        }
        else {
            return Map.of("message", "много запросов");
        }
    }

    @ExceptionHandler
    private ResponseEntity<ClientResponse> handleException(ClientAlreadyExistAuthenticationException e){
        ClientResponse clientResponse = new ClientResponse("Пользователь с таким номером телефона уже существует", System.currentTimeMillis());
        return new ResponseEntity<>(clientResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    private ResponseEntity<ClientResponse> handleException(BadCredentialsException e){
        ClientResponse clientResponse = new ClientResponse("Неправильный логин или пароль", System.currentTimeMillis());
        return new ResponseEntity<>(clientResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler ResponseEntity<ClientResponse> handleException(ClientValidationError e){
        String k = e.getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("; "));
        ClientResponse clientResponse = new ClientResponse(k, System.currentTimeMillis());
        return new ResponseEntity<>(clientResponse, HttpStatus.BAD_REQUEST);
    }

    public ClientInfo convertToClient(AuthenticationDTO authenticationDTO){
        return this.modelMapper.map(authenticationDTO, ClientInfo.class);
    }
}
