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
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.BucketDTOs.BucketItemAddDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.BucketDTOs.BucketItemDeleteDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.BucketDTOs.BucketShowItemDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.*;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.BucketItemService;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.BucketService;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.Client.ClientService;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.ProductService;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.ProductVariantService;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.CountException;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.NotFoundException;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    public ResponseEntity<Void> deleteProductUnitFromClientCard(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody @Valid BucketItemDeleteDTO bucketItemDeleteDTO, BindingResult bindingResult) {
        handleBucketItemDeleteRequests(token, bucketItemDeleteDTO, bindingResult, (client, product, productVariant) ->
                bucketItemService.updateDeleteOrDecreaseBucketItem(client.getBucket(), product, productVariant)
        );
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/reset")
    public ResponseEntity<Void> resetProductUnitFromClientCard(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody @Valid BucketItemDeleteDTO bucketItemDeleteDTO, BindingResult bindingResult){
        handleBucketItemDeleteRequests(token, bucketItemDeleteDTO, bindingResult, (client, product, productVariant) ->
                bucketItemService.resetBucketItem(client.getBucket(), product, productVariant)
        );
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/showBucket")
    public ResponseEntity<List<BucketShowItemDTO>> showClientBucketProducts(@RequestHeader(HttpHeaders.AUTHORIZATION) String token)   {
          Client client = clientService.findByPhoneNumber(clientService.getPhoneNumberFromToken(token));
          Bucket bucket = client.getBucket();
        List<BucketShowItemDTO> showItemDTOSet = bucket.getBucketItemSet().stream()
                .map(bucketItem -> new BucketShowItemDTO(
                        bucketItem.getProduct().getProductName(),
                        bucketItem.getProductVariant().getProductVariantName(),
                        bucketItem.getQuantity(),
                        bucketItem.getProductVariant().getProductVariantPrice(),
                        bucketItem.getItemPrice(),
                        bucketItem.getProduct().getImageName()
                ))
                .collect(Collectors.toList());

          return new ResponseEntity<>(showItemDTOSet, HttpStatus.OK);
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
