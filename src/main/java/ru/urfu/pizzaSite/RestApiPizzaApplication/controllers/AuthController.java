package ru.urfu.pizzaSite.RestApiPizzaApplication.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.AuthenticationDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.ClientDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Client;
import ru.urfu.pizzaSite.RestApiPizzaApplication.security.JWTUtil;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.RegistrationService;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.ClientValidator;

import java.util.Map;

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
            Client client = convertToClient(clientDTO);
            clientValidator.validate(client, bindingResult);
            if (bindingResult.hasErrors()){
                //TODO сделать handler ошибки регистрации
                Map.of("message", "error");
            }
            registrationService.register(client);
            String token = jwtUtil.generateToken(client.getPhoneNumber());
            return Map.of("jwt-token", token);
    }
    @PostMapping("/login")
    public Map<String, String> performLogin(@RequestBody AuthenticationDTO authenticationDTO){
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getPhoneNumber(), authenticationDTO.getPassword());
        try {
            //TODO сделать handler ошибки логина
            authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e){
            return Map.of("message", "Incorrect credentials!");
        }
        String token = jwtUtil.generateToken(authenticationDTO.getPhoneNumber());
        return Map.of("jwt-token", token);
    }

    public Client convertToClient(ClientDTO clientDTO){
        return this.modelMapper.map(clientDTO, Client.class);
    }
}
