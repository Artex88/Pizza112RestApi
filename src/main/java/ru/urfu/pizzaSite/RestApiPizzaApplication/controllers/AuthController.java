package ru.urfu.pizzaSite.RestApiPizzaApplication.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.urfu.pizzaSite.RestApiPizzaApplication.api.SMSApi;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.ClientDTOs.AuthenticationDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.ClientDTOs.ClientDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Client;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.ClientInfo;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.ClientResponse;
import ru.urfu.pizzaSite.RestApiPizzaApplication.security.LongGenerator;
import ru.urfu.pizzaSite.RestApiPizzaApplication.security.TOTPGenerator;
import ru.urfu.pizzaSite.RestApiPizzaApplication.security.JWTUtil;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.Client.ClientService;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.Client.ClientInfoService;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.Client.RegistrationService;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.AuthorizationAttemptsExhaustedException;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.ValidationException;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.TooManyRequestException;


import java.security.InvalidKeyException;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Аутентификация и авторизация", description = "Методы для работы с аутентификацией и авторизацией клиентов")
public class AuthController {
    private final RegistrationService registrationService;

    private final LongGenerator longGenerator;
    private final JWTUtil jwtUtil;

    private final ClientService clientService;

    private final ClientInfoService clientInfoService;

    private final TOTPGenerator TOTPGenerator;

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;



    @Autowired
    public AuthController(RegistrationService registrationService, LongGenerator longGenerator, JWTUtil jwtUtil, ClientService clientService, ClientInfoService clientInfoService, SMSApi smsApi, TOTPGenerator TOTPGenerator, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.registrationService = registrationService;
        this.longGenerator = longGenerator;
        this.jwtUtil = jwtUtil;
        this.clientService = clientService;
        this.clientInfoService = clientInfoService;
        this.TOTPGenerator = TOTPGenerator;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }
    @PostMapping("/sms_authentications")
    @Operation(summary = "Отправка сообщения с одноразовым кодом на номер телефона")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Пример запроса на отправку сообщения с кодом для спец. номера +79999999999", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(
                    summary = "Пример запроса на спец номер +79999999999",
                    value = "{\"phoneNumber\": 79999999999}"
            ))
    })
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Код отправлен на указаный номер телефона", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(
                            summary = "Пример ответа, что сообщение отправленно на конкретный номер",
                            value = "{\"message\": Message send , \"timestamp\": 123456789}"
                    ))
            }),
            @ApiResponse(responseCode = "429", description = "При попытке отправлять новое сообщение чаще, чем за одну минуту",content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(
                            summary = "Пример ответа на ошибку 429",
                            value = "{\"message\": Too many requests, please wait one minute , \"timestamp\": 123456789}"
                    ))
            }),
            @ApiResponse(responseCode = "400", description = "Некорректно введен номер телефона( должен быть без плюса, 11 символов)",content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(
                            summary = "Пример ответа на ошибку 400",
                            value = "{\"message\": The number must contain exactly 11 digits , \"timestamp\": 123456789}"
                    ))
            })
    }
    )
    public ResponseEntity<ClientResponse> performRegistration(@RequestBody @Valid @Parameter(description = "Сущность клиента, содержащая номер телефона") ClientDTO clientDTO, BindingResult bindingResult) throws InvalidKeyException {
            if (bindingResult.hasErrors())
                throw new ValidationException(bindingResult);

            clientService.sendRegistrationMessage(clientDTO.getPhoneNumber(), TOTPGenerator.generatePassword(longGenerator.generateLong()));
            return new ResponseEntity<>(new ClientResponse("Message send", System.currentTimeMillis()), HttpStatus.OK);
    }


    @PostMapping("/sms_check")
    @Operation(summary = "Получение номера телефона и кода. Если всё верно, возвращает jwt токен. Для номера телефона 79999999999 существует постояный код 111111.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Пример запроса на авторизацию для спец. номера +79999999999", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(
                    summary = "Пример запроса авторизации на спец номер +79999999999",
                    value = "{\"phoneNumber\": 79999999999 , \"password\": 111111}"
            ))
    })
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Код и номер телефона совпадают, возвращается jwt токен", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(
                            summary = "Пример ответа на спец. номер 79999999999, если номер и код правильные",
                            value = "{\"jwt-token\": mtprrwlko4032krwmekogeoi24grgw...}"
                    ))
            }),
            @ApiResponse(responseCode = "429", description = "Когда пользователь потратил все 3 доступные попытки на ввод. Требуется ещё раз отправить сообщение", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(
                            summary = "Пример ответа, если пользователь потратил все попытки",
                            value = "{\"message\": You have spent all possible attempts, resend the message , \"timestamp\": 123456789}"
                    ))
            }),
            @ApiResponse(responseCode = "400", description = "Некорректно введен номер телефона( должен быть без плюса, 11 символов) или код(6 символов)",content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(
                            summary = "Пример ответа, если пользователь неправильно ввел номер телефона или код",
                            value = "{\"message\": Incorrect code or login , \"timestamp\": 123456789}"
                    ))
            })
    }
    )
    public Map<String, String> performLogin(@RequestBody @Valid @Parameter(description = "Сущность клиента, содержащая номер телефона и одноразовый код") AuthenticationDTO authenticationDTO , BindingResult bindingResult) throws InvalidKeyException {
        if (bindingResult.hasErrors())
            throw new ValidationException(bindingResult);

        Client client = clientService.findByPhoneNumber(authenticationDTO.getPhoneNumber());
        clientService.validateLoginRequest(client, passwordEncoder.encode(TOTPGenerator.generatePassword(longGenerator.generateLong())));

        if (clientInfoService.isClientExist(authenticationDTO.getPhoneNumber())) {
                    clientService.authenticate(authenticationDTO, client,  passwordEncoder.encode(TOTPGenerator.generatePassword(longGenerator.generateLong())));
                } else {
                    clientService.authenticate(authenticationDTO,client, passwordEncoder.encode(TOTPGenerator.generatePassword(longGenerator.generateLong())));
                    ClientInfo newClientInfo = convertToClient(authenticationDTO);
                    registrationService.register(newClientInfo, client);
                }
        String token = jwtUtil.generateToken(client.getPhoneNumber());
        return Map.of("jwt-token", token);
    }


    @PostMapping("/sms_check/resend")
    @Operation(summary = "Повторная отпрвка сообщения с одноразовым кодом на номер телефона")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Пример запроса на повторную отправку кода для спец. номера +79999999999", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(
                    summary = "Пример запроса на повт. отправку кода на спец номер +79999999999",
                    value = "{\"phoneNumber\": 79999999999}"
            ))
    })
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Повторная отправка сообщения с кодом на номер телефона", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(
                            summary = "Пример ответа, что сообщение отправленно на конкретный номер",
                            value = "{\"message\": Message send , \"timestamp\": 123456789}"
                    ))
            }),
            @ApiResponse(responseCode = "429", description = "При попытке отправлять новое сообщение чаще, чем за одну минуту",content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(
                            summary = "Пример ответа на ошибку 429",
                            value = "{\"message\": Too many requests, please wait one minute , \"timestamp\": 123456789}"
                    ))
            }),
            @ApiResponse(responseCode = "400", description = "Некорректно введен номер телефона( должен быть без плюса, 11 символов)",content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(
                            summary = "Пример ответа на ошибку 400",
                            value = "{\"message\": The number must contain exactly 11 digits , \"timestamp\": 123456789}"
                    ))
            })
    }
    )
    public ResponseEntity<ClientResponse> resendLogin(@RequestBody @Valid @Parameter(description = "Сущность клиента, содержащая номер телефона") ClientDTO clientDTO) throws InvalidKeyException{
        clientService.sendRegistrationMessage(clientDTO.getPhoneNumber(),TOTPGenerator.generatePassword(longGenerator.generateLong()));
        return new ResponseEntity<>(new ClientResponse("Message send", System.currentTimeMillis()), HttpStatus.OK);
    }

    @ExceptionHandler(AuthorizationAttemptsExhaustedException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    private ResponseEntity<ClientResponse> handleException(AuthorizationAttemptsExhaustedException e){
        ClientResponse clientResponse = new ClientResponse("You have spent all possible attempts, resend the message", System.currentTimeMillis());
        return new ResponseEntity<>(clientResponse, HttpStatus.TOO_MANY_REQUESTS);
    }
    @ExceptionHandler(TooManyRequestException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    private ResponseEntity<ClientResponse> handleException(TooManyRequestException e){
        ClientResponse clientResponse = new ClientResponse("Too many requests, please wait one minute", System.currentTimeMillis());
        return new ResponseEntity<>(clientResponse, HttpStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<ClientResponse> handleException(BadCredentialsException e){
        ClientResponse clientResponse = new ClientResponse("Incorrect code or login", System.currentTimeMillis());
        return new ResponseEntity<>(clientResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<ClientResponse> handleException(ValidationException e){
        String k = e.getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("; "));
        ClientResponse clientResponse = new ClientResponse(k, System.currentTimeMillis());
        return new ResponseEntity<>(clientResponse, HttpStatus.BAD_REQUEST);
    }

    public ClientInfo convertToClient(AuthenticationDTO authenticationDTO){
        return this.modelMapper.map(authenticationDTO, ClientInfo.class);
    }
}
