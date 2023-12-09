package ru.urfu.pizzaSite.RestApiPizzaApplication.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.ProductDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.ShowDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Product;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.ProductService;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.ClientResponse;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.SortException;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.validators.ShowDTOValidator;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.CountException;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.ShowDTOValidationException;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/show")
public class ShowController {

    private final ShowDTOValidator showDTOValidator;

    private final ProductService productService;

    private final ModelMapper modelMapper;

    @Autowired
    public ShowController(ShowDTOValidator showDTOValidator, ProductService productService, ModelMapper modelMapper) {
        this.showDTOValidator = showDTOValidator;
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @PostMapping()
    public ResponseEntity<List<ProductDTO>> showProducts(@RequestBody ShowDTO showDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            throw new ShowDTOValidationException(bindingResult);
        showDTOValidator.validate(showDTO,bindingResult);
        Pageable pageable = productService.getPageable(showDTO);
        List<ProductDTO> productList =  productService.findAllCertainProductsAndDisplayCertainCount(showDTO.getFrom(), pageable).stream().map(this::convertToProductDTO).toList();
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }
    public ProductDTO convertToProductDTO(Product product){
        return this.modelMapper.map(product, ProductDTO.class);
    }

    @ExceptionHandler(ShowDTOValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<ClientResponse> handleException(ShowDTOValidationException e){
        String k = e.getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("; "));
        ClientResponse clientResponse = new ClientResponse(k, System.currentTimeMillis());
        return new ResponseEntity<>(clientResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CountException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<ClientResponse> handleException(CountException e){
        ClientResponse clientResponse = new ClientResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(clientResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SortException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<ClientResponse> handleException(SortException e){
        ClientResponse clientResponse = new ClientResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(clientResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<ClientResponse> handleException(HttpMessageNotReadableException e){
        ClientResponse clientResponse = new ClientResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(clientResponse, HttpStatus.BAD_REQUEST);
    }

}
