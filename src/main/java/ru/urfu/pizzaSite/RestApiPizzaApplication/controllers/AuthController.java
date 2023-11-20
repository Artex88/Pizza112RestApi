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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.AuthenticationDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.ClientDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Client;
import ru.urfu.pizzaSite.RestApiPizzaApplication.security.JWTUtil;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.RegistrationService;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.ClientErrorResponse;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.ClientValidationError;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.ClientValidator;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.ClientAlreadyExistAuthenticationException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final RegistrationService registrationService;
    private final ClientValidator clientValidator;
    private final JWTUtil jwtUtil;

    private final ModelMapper modelMapper;

    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(RegistrationService registrationService, ClientValidator clientValidator, JWTUtil jwtUtil, ModelMapper modelMapper, AuthenticationManager authenticationManager) {
        this.registrationService = registrationService;
        this.clientValidator = clientValidator;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;

        this.authenticationManager = authenticationManager;
    }
    @PostMapping("/registration")
    public Map<String, String> performRegistration(@RequestBody @Valid ClientDTO clientDTO, BindingResult bindingResult){
            if (bindingResult.hasErrors()){
                throw new ClientValidationError(bindingResult);
            }
            Client client = convertToClient(clientDTO);
            clientValidator.validate(client, bindingResult);

            registrationService.register(client);
            String token = jwtUtil.generateToken(client.getPhoneNumber());
            return Map.of("jwt-token", token);
    }
    @PostMapping("/login")
    public Map<String, String> performLogin(@RequestBody AuthenticationDTO authenticationDTO){
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getPhoneNumber(), authenticationDTO.getPassword());
        authenticationManager.authenticate(authenticationToken);
        String token = jwtUtil.generateToken(authenticationDTO.getPhoneNumber());
        return Map.of("jwt-token", token);
    }
    @ExceptionHandler
    private ResponseEntity<ClientErrorResponse> handleException(ClientAlreadyExistAuthenticationException e){
        ClientErrorResponse clientErrorResponse = new ClientErrorResponse("Пользователь с таким номером телефона уже существует", System.currentTimeMillis());
        return new ResponseEntity<>(clientErrorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    private ResponseEntity<ClientErrorResponse> handleException(BadCredentialsException e){
        ClientErrorResponse clientErrorResponse = new ClientErrorResponse("Неправильный логин или пароль", System.currentTimeMillis());
        return new ResponseEntity<>(clientErrorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler ResponseEntity<ClientErrorResponse> handleException(ClientValidationError e){
        String k = e.getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("; "));
        ClientErrorResponse clientErrorResponse = new ClientErrorResponse(k, System.currentTimeMillis());
        return new ResponseEntity<>(clientErrorResponse, HttpStatus.BAD_REQUEST);
    }
    public Client convertToClient(ClientDTO clientDTO){
        return this.modelMapper.map(clientDTO, Client.class);
    }
}
