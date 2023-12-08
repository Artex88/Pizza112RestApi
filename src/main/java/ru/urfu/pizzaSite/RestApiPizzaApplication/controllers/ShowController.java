package ru.urfu.pizzaSite.RestApiPizzaApplication.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Pageable;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.ProductDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.ShowDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Product;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.ProductService;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.ShowDTOValidator;


import java.util.List;

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
        showDTOValidator.validate(showDTO,bindingResult);
        Pageable pageable = productService.getPageable(showDTO);
        List<ProductDTO> productList =  productService.findAllCertainProductsAndDisplayCertainCount(showDTO.getFrom(), pageable).stream().map(this::convertToProductDTO).toList();
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }


    public ProductDTO convertToProductDTO(Product product){
        return this.modelMapper.map(product, ProductDTO.class);
    }
}
