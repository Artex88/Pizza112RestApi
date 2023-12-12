package ru.urfu.pizzaSite.RestApiPizzaApplication.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.ClientInfoDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Client;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.ClientInfo;
import ru.urfu.pizzaSite.RestApiPizzaApplication.security.JWTUtil;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.Client.ClientInfoService;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.Client.ClientService;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.ClientResponse;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.ValidationException;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.NotFoundException;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/client")
@Tag(name = "Контроллер работы с клиентом", description = "Методы для работы с личными данными клиента")
public class ClientController {
    private final ClientInfoService clientInfoService;

    private final JWTUtil jwtUtil;

    private final ClientService clientService;


    @Autowired
    public ClientController(ClientInfoService clientInfoService, JWTUtil jwtUtil, ClientService clientService) {
        this.clientInfoService = clientInfoService;
        this.jwtUtil = jwtUtil;
        this.clientService = clientService;
    }

    @PostMapping("/updatePP")
    @Operation(summary = "Обновление всех переданных текстовых полей клиента, кроме аватара. Для идентификации пользователя необходимо передавать в headers jwt-token (в хедере Authorization)")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Пример запроса на обновление некоторых полей пользователя", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(
                    summary = "Пример запроса обновление полей",
                    value = "{\"name\": Вася, \"surname\": Петров}"
            ))
    })
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Все переданные поля прошли валидацию и обновились в БД (НЕ ПЕРЕДАВАЛСЯ НОМЕР ТЕЛЕФОНА)"),
            @ApiResponse(responseCode = "200", description = "Все переданные поля прошли валидацию и обновились в БД (НЕ ПЕРЕДАВАЛСЯ НОМЕР ТЕЛЕФОНА)", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(
                            name = "Пример ответа, где передавался номер телефона. Возвращает новый jwt-token, который надо обновить в headers",
                            summary = "Пример ответа, где передавался номер телефона. Возвращает новый jwt-token, который надо обновить в headers",
                            value = "{\"message\": mtprrwlko4032krwmekogeoi24grgw...}"
                    ))
            }),
            @ApiResponse(responseCode = "400", description = "Возможные варианты, когда выбрасывается ошибка 400 " +
                    "\n 1. Не прошла валидация для полей clientInfoDTO;" +
                    "\n 2. Дату рождения клиента нужно передевать строго в формате 1970-MM-dd, где mm и dd указываются пользователем."),
            @ApiResponse(responseCode = "403", description = "Возможные варианты, когда выбрасывается ошибка 403" +
                    "\n 1. Проблема с jwt-token (просрочен, не валиден, отсутствует)"),
            @ApiResponse(responseCode = "404", description = "Возможные варианты, когда выбрасывается ошибка 404" +
                    "\n 1. Пользователя, номер, которого вы передали в jwt токене не существует."),
            @ApiResponse(responseCode = "500", description = "-------")
    }
    )
    public ResponseEntity<ClientResponse> updatePP(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody @Valid ClientInfoDTO clientInfoDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            throw new ValidationException(bindingResult);
        String phoneNumber = clientService.getPhoneNumberFromToken(token);
        Client client = clientService.findByPhoneNumber(phoneNumber);

        clientService.save(clientInfoService.updateClientInfo(client, clientInfoDTO));
        if (clientInfoDTO.getPhoneNumber() != null){
            return new ResponseEntity<>(new ClientResponse(jwtUtil.generateToken(clientInfoDTO.getPhoneNumber()), System.currentTimeMillis()), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/updatePPAvatar")
    @Operation(summary = "Обновление аватара пользователя. Для идентификации пользователя необходимо передавать в headers jwt-token (в хедере Authorization)\"",
            description = "Чтобы запрос на обновление прошёл успешно необходимо следующие:" +
                    "\n 1. Content-type в headers должен быть multipart/form-data"+
                    "\n 2. Файл должен быть передан с именем 'file' ; " +
                    "\n 3. Разрешаемые форматы файла: png, jpeg, webp(предподчительно); " +
                    "\n 4. Размер файла не более 200-т килобайт.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Аватар успешно обновлён"),
            @ApiResponse(responseCode = "403", description = "Возможные варианты, когда выбрасывается ошибка 403" +
                    "\n 1. Проблема с jwt-token (просрочен, не валиден, отсутсвует)"),
            @ApiResponse(responseCode = "400", description = "Возможные варианты, когда выбрасывается ошибка 404" +
                    "\n 1.Файл не прошёл валидацию(валидация указывается в описании)"),
            @ApiResponse(responseCode = "500", description = "Возможные варианты, когда выбрасывается ошибка 500: " +
                    "\n 1. Серверная ошибка обработки изображения."),
            @ApiResponse(responseCode = "404", description = "Возможные варианты, когда выбрасывается ошибка 404" +
                    "\n 1. Пользователя, номер, которого вы передали в jwt токене не существует.")
    })
    public ResponseEntity<Void> updatePPAvatar(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestHeader(HttpHeaders.CONTENT_TYPE) String contentType, @RequestParam("file") MultipartFile multipartFile) throws IOException {
        if (contentType != null && contentType.startsWith("multipart/form-data")){
            String phoneNumber = clientService.getPhoneNumberFromToken(token);
            ClientInfo clientInfo = clientInfoService.findByPhoneNumber(phoneNumber);
            clientInfoService.updateAvatar(clientInfo, multipartFile);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/getPP")
    @Operation(summary = "Получение всех полей клиента(как получить изображение аватар читать подробнее в ClientInfoDTO). Для идентификации пользователя необходимо передавать в headers jwt-token (в хедере Authorization)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошёл успешно", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(
                            summary = "Пример ответа, где передаются все поля клиента",
                            value = """
                                    {   "patronymic": "Петрович",
                                        "phoneNumber": "79999999999",
                                        "surname": "Ивааанов",
                                        "name": "Петр",
                                        "dateOfBirth": "1971-02-14",
                                        "photoName": "default.webp",
                                        "email": null  \s
                                        }"""
                    ))
            }),
            @ApiResponse(responseCode = "403", description = "Возможные варианты, когда выбрасывается ошибка 403: " +
                    "\n 1. Проблема с jwt-token (просрочен, не валиден, отсутствует)"),
            @ApiResponse(responseCode = "404", description = "Возможные варианты, когда выбрасывается ошибка 404: " +
                    "\n 1. Пользователя, номер, которого вы передали в jwt токене не существует."),
            @ApiResponse(responseCode = "400" , description = "-------"),
            @ApiResponse(responseCode = "500", description = "-------")
    })

    public Map<String, String> showPP(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String phoneNumber = clientService.getPhoneNumberFromToken(token);
        ClientInfo clientInfo = clientInfoService.findByPhoneNumber(phoneNumber);

        return clientInfoService.fillClientInfoJSON(clientInfo);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<ClientResponse> handleException(ValidationException e){
        String k = e.getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("; "));
        ClientResponse clientResponse = new ClientResponse(k, System.currentTimeMillis());
        return new ResponseEntity<>(clientResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ResponseEntity<ClientResponse> handleException(NotFoundException e){
        ClientResponse clientResponse = new ClientResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(clientResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<ClientResponse> handleException(IOException e){
        ClientResponse clientResponse = new ClientResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(clientResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<ClientResponse> handleException(HttpMessageNotReadableException e){
        ClientResponse clientResponse = new ClientResponse("The date must be transmitted in the format \"1970-MM-dd\"", System.currentTimeMillis());
        return new ResponseEntity<>(clientResponse, HttpStatus.BAD_REQUEST);
    }
}
