package ru.urfu.pizzaSite.RestApiPizzaApplication.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.BucketDTOs.BucketItemAddDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.BucketDTOs.BucketItemDeleteDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.BucketDTOs.BucketShowItemDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.*;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.Bucket.BucketItemService;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.Bucket.BucketService;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.Client.ClientService;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.Product.ProductService;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.Product.ProductVariantService;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.CountException;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.NotFoundException;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.ValidationException;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bucket")
public class BucketController {

    private final BucketService bucketService;

    private final ProductVariantService productVariantService;

    private final BucketItemService bucketItemService;

    private final ProductService productService;

    private final ClientService clientService;

    @Autowired
    public BucketController(BucketService bucketService, ProductVariantService productVariantService, BucketItemService bucketItemService, ProductService productService, ClientService clientService) {
        this.bucketService = bucketService;
        this.productVariantService = productVariantService;
        this.bucketItemService = bucketItemService;
        this.productService = productService;
        this.clientService = clientService;
    }

    @PostMapping("/add")
    @Operation(summary = "Добавление n-ого кол-ва определенного продукта в корзину пользователя(нужна авторизация)(используется BucketItemAddDTO)")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Пример запроса на добавление продукта", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(
                    summary = "пример запроса",
                    value = """
                            {
                                "productId": 0,
                                "quantity": 2,
                                "productVariant": "SP"
                            }
                            """
            ))
    })
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Успешное добавление в базу"),

            @ApiResponse(responseCode = "404", description = "Возможные варианты, когда выбрасывается ошибка 404 " +
                    "\n 1. Продукта с id, который передаеться, не существует." +
                    "\n 2. Пользователя, номер, которого вы передали в jwt токене не существует." +
                    "\n 3. Варианта продукта, который передается, не существует у данного продукта." +
                    "\n 4. Ошибка валидации."),

            @ApiResponse(responseCode = "403", description = "Возможные варианты, когда выбрасывается ошибка 403: " +
                    "\n 1. Проблема с jwt-token (просрочен, не валиден, отсутствует)")
    }
    )
    public ResponseEntity<Void> addProductToClientBucket(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody @Valid BucketItemAddDTO bucketItemAddDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new ValidationException(bindingResult);

        Product product = productService.findById(bucketItemAddDTO.getProductId());
        Client client = clientService.findByPhoneNumber(clientService.getPhoneNumberFromToken(token));

        ProductVariant productVariant = productVariantService.getProductVariantFromProduct(bucketItemAddDTO.getProductVariant(), product);
        createClientBucketIfItDoesNotExist(client);
        bucketItemService.updateAddIncreaseBucketItem(productVariant, product, client.getBucket(), bucketItemAddDTO.getQuantity());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/delete")
    @Operation(summary = "Удаление одной единицы продукта из корзины пользователя(нужна авторизация)(используется BucketItemDeleteDTO)")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Пример запроса на удаление 1 единицы продукта или всего, если осталась 1 единица", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(
                    summary = "пример запроса",
                    value = """
                            {
                                "productId": 0,
                                "productVariant": "SP"
                            }
                            """
            ))
    })
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Продукт успешно удален или его количество уменьшено на единицу"),

            @ApiResponse(responseCode = "404", description = "Возможные варианты, когда выбрасывается ошибка 404 " +
                    "\n 2. Пользователя, номер, которого вы передали в jwt токене не существует." +
                    "\n 3. Варианта продукта или продукта, который передается на удаление нету в корзине." +
                    "\n 4. У данного пользователя не существует активной корзины." +
                    "\n 5. Ошибка валидации"),

            @ApiResponse(responseCode = "403", description = "Возможные варианты, когда выбрасывается ошибка 403: " +
                    "\n 1. Проблема с jwt-token (просрочен, не валиден, отсутствует)")
    }
    )
    public ResponseEntity<Void> deleteProductUnitFromClientCard(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody @Valid BucketItemDeleteDTO bucketItemDeleteDTO, BindingResult bindingResult) {
        handleBucketItemDeleteRequests(token, bucketItemDeleteDTO, bindingResult, (client, product, productVariant) ->
                bucketItemService.updateDeleteOrDecreaseBucketItem(client.getBucket(), product, productVariant)
        );
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/reset")
    @Operation(summary = "Удаление всего продукта из корзины пользователя(нужна авторизация)(используется BucketItemDeleteDTO)")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Пример запроса на полное удаление, независимо от его количества продукта", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(
                    summary = "пример запроса",
                    value = """
                            {
                                "productId": 0,
                                "productVariant": "SP"
                            }
                            """
            ))
    })
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Продукт успешно полностью удален из корзины"),

            @ApiResponse(responseCode = "404", description = "Возможные варианты, когда выбрасывается ошибка 404 " +
                    "\n 2. Пользователя, номер, которого вы передали в jwt токене не существует." +
                    "\n 3. Варианта продукта или продукта, который передается на удаление нету в корзине." +
                    "\n 4. У данного пользователя не существует активной корзины." +
                    "\n 5. Ошибка валидации"),

            @ApiResponse(responseCode = "403", description = "Возможные варианты, когда выбрасывается ошибка 403: " +
                    "\n 1. Проблема с jwt-token (просрочен, не валиден, отсутствует)")
    }
    )
    public ResponseEntity<Void> resetProductUnitFromClientCard(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody @Valid BucketItemDeleteDTO bucketItemDeleteDTO, BindingResult bindingResult){
        handleBucketItemDeleteRequests(token, bucketItemDeleteDTO, bindingResult, (client, product, productVariant) ->
                bucketItemService.resetBucketItem(client.getBucket(), product, productVariant)
        );
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void handleBucketItemDeleteRequests(String token, BucketItemDeleteDTO bucketItemDeleteDTO, BindingResult bindingResult, BucketItemAction bucketItemAction ){
        if (bindingResult.hasErrors())
            throw new ValidationException(bindingResult);

        Client client = clientService.findByPhoneNumber(clientService.getPhoneNumberFromToken(token));
        if (client.getBucket() == null)
            throw new NotFoundException("This client doesn't have active bucket");

        Product product = productService.findById(bucketItemDeleteDTO.getProductId());
        ProductVariant productVariant = productVariantService.getProductVariantFromProduct(bucketItemDeleteDTO.getProductVariant(), product);
        bucketItemAction.perform(client, product, productVariant);
    }

    @GetMapping("/showBucket")
    @Operation(summary = "Получение активной корзины клиента. Для идентификации пользователя необходимо передавать в headers jwt-token (в хедере Authorization)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Корзина не пустая и запрос прошёл успешно", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(
                            summary = "Пример ответа, где передается активная корзина клиента",
                            value = """
                                    [
                                        {
                                           "name": "Мясная",
                                           "productVariant": "SP",
                                           "quantity": 10,
                                           "itemPrice": 499.0,
                                           "productVariantPrice": 4990.0,
                                           "image": "0.webp",
                                           "productId": 0
                                        },
                                        {
                                           "name": "Сырные чипсы",
                                           "productVariant": "B",
                                           "quantity": 5,
                                           "itemPrice": 105.0,
                                           "productVariantPrice": 525.0,
                                           "image": "3.webp",
                                           "productId": 3
                                         }
                                    ]"""
                    ))
            }),
            @ApiResponse(responseCode = "403", description = "Возможные варианты, когда выбрасывается ошибка 403: " +
                    "\n 1. Проблема с jwt-token (просрочен, не валиден, отсутствует)"),
            @ApiResponse(responseCode = "404", description = "Возможные варианты, когда выбрасывается ошибка 404: " +
                    "\n 1. Пользователя, номер, которого вы передали в jwt токене не существует.")
    })
    public ResponseEntity<List<BucketShowItemDTO>> showClientBucketProducts(@RequestHeader(HttpHeaders.AUTHORIZATION) String token)   {
        Client client = clientService.findByPhoneNumber(clientService.getPhoneNumberFromToken(token));

        if (client.getBucket() == null)
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);

        Bucket bucket = client.getBucket();
        List<BucketShowItemDTO> showItemDTOSet = bucket.getBucketItemSet().stream()
                .map(bucketItem -> new BucketShowItemDTO(
                        bucket.getId(),
                        bucketItem.getProduct().getProductName(),
                        bucketItem.getProductVariant().getProductVariantName(),
                        bucketItem.getQuantity(),
                        bucketItem.getProductVariant().getProductVariantPrice(),
                        bucketItem.getItemPrice(),
                        bucketItem.getProduct().getImageName(),
                        bucketItem.getProduct().getId()
                ))
                .collect(Collectors.toList());

        showItemDTOSet = showItemDTOSet.stream()
                .sorted(Comparator.comparing(BucketShowItemDTO::getName))
                .collect(Collectors.toList());

        return new ResponseEntity<>(showItemDTOSet, HttpStatus.OK);
    }

    @FunctionalInterface
    private interface BucketItemAction {
        void perform(Client client, Product product, ProductVariant productVariant);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<ClientResponse> handleException(ValidationException e){
        String k = e.getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("; "));
        ClientResponse clientResponse = new ClientResponse(k, System.currentTimeMillis());
        return new ResponseEntity<>(clientResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CountException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<ClientResponse> handleException(CountException e){
        ClientResponse clientResponse = new ClientResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(clientResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ResponseEntity<ClientResponse> handleException(NotFoundException e){
        ClientResponse clientResponse = new ClientResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(clientResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<ClientResponse> handleException(NullPointerException e){
        ClientResponse clientResponse = new ClientResponse("This product must have productType", System.currentTimeMillis());
        return new ResponseEntity<>(clientResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<ClientResponse> handleException(HttpMessageNotReadableException e){
        ClientResponse clientResponse = new ClientResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(clientResponse, HttpStatus.BAD_REQUEST);
    }

    private void createClientBucketIfItDoesNotExist(Client client) {
        if (client.getBucket() == null)
            bucketService.createBucket(client);
    }
}
