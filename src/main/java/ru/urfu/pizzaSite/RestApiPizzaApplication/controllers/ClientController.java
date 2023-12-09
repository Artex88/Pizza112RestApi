package ru.urfu.pizzaSite.RestApiPizzaApplication.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.ClientInfoDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Client;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.ClientInfo;
import ru.urfu.pizzaSite.RestApiPizzaApplication.security.JWTUtil;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.ClientInfoService;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.ClientService;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.ClientResponse;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.ClientValidationException;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.NotFoundException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/client")
public class ClientController {
    private final ModelMapper modelMapper;
    private final ClientInfoService clientInfoService;

    private final JWTUtil jwtUtil;

    private final ClientService clientService;



    @Autowired
    public ClientController(ModelMapper modelMapper, ClientInfoService clientInfoService, JWTUtil jwtUtil, ClientService clientService) {
        this.modelMapper = modelMapper;
        this.clientInfoService = clientInfoService;
        this.jwtUtil = jwtUtil;
        this.clientService = clientService;
    }

    @PostMapping("/updatePP")
    public ResponseEntity<ClientResponse> updatePP(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody @Valid ClientInfoDTO clientInfoDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            throw new ClientValidationException(bindingResult);
        String phoneNumber = clientInfoService.getPhoneNumberFromToken(token);
        Client client = clientService.findByPhoneNumber(phoneNumber);

        clientService.save(clientInfoService.updateClientInfo(client, clientInfoDTO));
        if (clientInfoDTO.getPhoneNumber() != null){
            return new ResponseEntity<>(new ClientResponse(jwtUtil.generateToken(clientInfoDTO.getPhoneNumber()), System.currentTimeMillis()), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/getPP")
    public Map<String, String> showPP(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String phoneNumber = clientInfoService.getPhoneNumberFromToken(token);
        ClientInfo clientInfo = clientInfoService.findByPhoneNumber(phoneNumber);

        return clientInfoService.fillClientInfoJSON(clientInfo);
    }




    @ExceptionHandler(ClientValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<ClientResponse> handleException(ClientValidationException e){
        String k = e.getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("; "));
        ClientResponse clientResponse = new ClientResponse(k, System.currentTimeMillis());
        return new ResponseEntity<>(clientResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<ClientResponse> handleException(NotFoundException e){
        ClientResponse clientResponse = new ClientResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(clientResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<ClientResponse> handleException(HttpMessageNotReadableException e){
        ClientResponse clientResponse = new ClientResponse("The date must be transmitted in the format \"1970-MM-dd\"", System.currentTimeMillis());
        return new ResponseEntity<>(clientResponse, HttpStatus.BAD_REQUEST);
    }


    public ClientInfo convertToClientInfo(ClientInfoDTO clientInfoDTO){
        return this.modelMapper.map(clientInfoDTO, ClientInfo.class);
    }
}
