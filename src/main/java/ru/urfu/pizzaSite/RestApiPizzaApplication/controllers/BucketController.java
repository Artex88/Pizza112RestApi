package ru.urfu.pizzaSite.RestApiPizzaApplication.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.BucketItemDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.*;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.BucketItemService;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.BucketService;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.Client.ClientService;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.ProductService;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.ProductVariantService;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.CountException;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.NotFoundException;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.ValidationException;

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
    public ResponseEntity<Void> addProductToClientBucket(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody @Valid BucketItemDTO bucketItemDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new ValidationException(bindingResult);

        Product product = productService.findById(bucketItemDTO.getProductId());
        Client client = clientService.findByPhoneNumber(clientService.getPhoneNumberFromToken(token));

        ProductVariant productVariant = productVariantService.getProductVariantFromProduct(bucketItemDTO, product);
        createClientBucketIfItDoesNotExist(client);
        bucketItemService.updateAddIncreaseBucketItem(productVariant, product, client.getBucket());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deleteProductUnitFromClientCard(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody @Valid BucketItemDTO bucketItemDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new ValidationException(bindingResult);

        Client client = clientService.findByPhoneNumber(clientService.getPhoneNumberFromToken(token));
        if (client.getBucket() == null)
            throw new NotFoundException("This client doesn't have active bucket");

        Product product = productService.findById(bucketItemDTO.getProductId());
        ProductVariant productVariant = productVariantService.getProductVariantFromProduct(bucketItemDTO, product);

        bucketItemService.updateDeleteOrDecreaseBucketItem(client.getBucket(), product, productVariant);
        return new ResponseEntity<>(HttpStatus.OK);
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
