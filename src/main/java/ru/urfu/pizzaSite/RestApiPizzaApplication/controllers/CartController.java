package ru.urfu.pizzaSite.RestApiPizzaApplication.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.CartItemDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.*;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.CartItemService;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.CartService;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.Client.ClientService;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.ProductService;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.ProductVariantService;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.NotFoundException;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.ValidationException;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.validators.CartItemValidator;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartItemValidator cartItemValidator;

    private final CartService cartService;

    private final ProductVariantService productVariantService;

    private final CartItemService cartItemService;

    private final ProductService productService;

    private final ClientService clientService;

    @Autowired
    public CartController(CartItemValidator cartItemValidator, CartService cartService, ProductVariantService productVariantService, CartItemService cartItemService, ProductService productService, ClientService clientService) {
        this.cartItemValidator = cartItemValidator;
        this.cartService = cartService;
        this.productVariantService = productVariantService;
        this.cartItemService = cartItemService;
        this.productService = productService;
        this.clientService = clientService;
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addProductToClientCart(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody CartItemDTO cartItemDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            throw new ValidationException(bindingResult);

        cartItemValidator.validate(cartItemDTO, bindingResult);
        Client client = clientService.findByPhoneNumber(clientService.getPhoneNumberFromToken(token));

        if (client.getCart() == null)
            cartService.createCart(client);

        Product product = productService.findById(cartItemDTO.getProductId());

        if (product.getProductVariants().isEmpty())
            cartItemService.updateCartItemByProduct(cartItemDTO, client, product);
         else {
            ProductVariant productVariant = productVariantService.getProductVariantFromProduct(cartItemDTO.getProductVariant().name(), product);
            cartItemService.updateCartItemByProductVariant(productVariant, cartItemDTO, product, client);
         }
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
