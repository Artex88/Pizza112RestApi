package ru.urfu.pizzaSite.RestApiPizzaApplication.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.*;


import java.security.InvalidKeyException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Аутентификация и авторизация", description = "Методы для работы с аутентификацией и авторизацией клиентов")
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

    private final PasswordEncoder passwordEncoder;



    @Autowired
    public AuthController(RegistrationService registrationService, ClientValidator clientValidator, LongGenerator longGenerator, JWTUtil jwtUtil, ClientService clientService, ClientInfoService clientInfoService, SMSApi smsApi, TOTPGenerator TOTPGenerator, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.registrationService = registrationService;
        this.clientValidator = clientValidator;
        this.longGenerator = longGenerator;
        this.jwtUtil = jwtUtil;
        this.clientService = clientService;
        this.clientInfoService = clientInfoService;
        this.smsApi = smsApi;
        this.TOTPGenerator = TOTPGenerator;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }
    @PostMapping("/sms_authentications")
    @Operation(summary = "Отправка сообщения с одноразовым кодом на номер телефона")
    public ResponseEntity<ClientResponse> performRegistration(@RequestBody @Valid @Parameter(description = "Сущность клиента, содержащая номер телефона") ClientDTO clientDTO, BindingResult bindingResult) throws InvalidKeyException {
            if (bindingResult.hasErrors()){
                throw new ClientValidationError(bindingResult);
            }

            clientService.sendRegistrationMessage(clientDTO.getPhoneNumber(), TOTPGenerator.generatePassword(longGenerator.generateLong()));
            return new ResponseEntity<>(new ClientResponse("Message send", System.currentTimeMillis()), HttpStatus.OK);
    }
    @PostMapping("/sms_check")
    @Operation(summary = "Получение номера телефона и кода. Если всё верно, возвращает jwt токен. Для номера телефона 79999999999 существует постояный код 111111.")
    public Map<String, String> performLogin(@RequestBody @Valid @Parameter(description = "Сущность клиента, содержащая номер телефона и одноразовый код") AuthenticationDTO authenticationDTO , BindingResult bindingResult) throws InvalidKeyException {

        if (bindingResult.hasErrors()){
            throw new ClientValidationError(bindingResult);
        }
        // TODO ПОЙМАТЬ ОШИБКУ, ЧТО ПОЛЬЗОВАЬЕЛЬ УЖЕ АВТОРИЗОВАН
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){
            Map.of("message", "пользователь уже авторизован");
        }

        Client client = clientService.findByPhoneNumber(authenticationDTO.getPhoneNumber());
        clientService.validateLoginRequest(client, passwordEncoder.encode(TOTPGenerator.generatePassword(longGenerator.generateLong())));

        if (clientInfoService.isClientExist(authenticationDTO.getPhoneNumber())) {
                    clientService.authenticate(authenticationDTO,client,  passwordEncoder.encode(TOTPGenerator.generatePassword(longGenerator.generateLong())));
                } else {
                    clientService.authenticate(authenticationDTO,client, passwordEncoder.encode(TOTPGenerator.generatePassword(longGenerator.generateLong())));
                    ClientInfo newClientInfo = convertToClient(authenticationDTO);
                    registrationService.register(newClientInfo, client);
                }
        String token = jwtUtil.generateToken(client.getId());
        return Map.of("jwt-token", token);

    }

    @PostMapping("/sms_check/resend")
    @Operation(summary = "Повторная отпрвка сообщения с одноразовым кодом на номер телефона" )
    public Map<String, String> resendLogin(@RequestBody @Valid @Parameter(description = "Сущность клиента, содержащая номер телефона") ClientDTO clientDTO) throws InvalidKeyException{
        clientService.sendRegistrationMessage(clientDTO.getPhoneNumber(),TOTPGenerator.generatePassword(longGenerator.generateLong()));
        return Map.of("message", "Message send");
    }

    @ExceptionHandler
    private ResponseEntity<ClientResponse> handleException(AuthorizationAttemptsExhaustedException e){
        ClientResponse clientResponse = new ClientResponse("You have spent all possible attempts, resend the message.", System.currentTimeMillis());
        return new ResponseEntity<>(clientResponse, HttpStatus.TOO_MANY_REQUESTS);
    }
    @ExceptionHandler
    private ResponseEntity<ClientResponse> handleException(TooManyRequestException e){
        ClientResponse clientResponse = new ClientResponse("Too many requsts, please wait one minute.", System.currentTimeMillis());
        return new ResponseEntity<>(clientResponse, HttpStatus.TOO_MANY_REQUESTS);
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
