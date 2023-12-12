package ru.urfu.pizzaSite.RestApiPizzaApplication.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.PizzaVariantDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.ProductDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.ShowDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.ShowProductDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Product;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.ProductType;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.ProductService;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.ClientResponse;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.enums.ProductTypes;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.SortException;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.validators.ShowDTOValidator;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.CountException;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.ShowDTOValidationException;


import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/show")
@Tag(name = "Контроллер общей информации", description = "Методы для работы с общедоступными данными")
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
    @Operation(summary = "Получение всех полей клиента(как получить изображение аватар из imageName читать подробнее в ClientInfoDTO). Для идентификации пользователя необходимо передавать в headers jwt-token (в хедере Authorization)")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Пример запроса на получение 2 товаров типа 'Pizza' без сортировки", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(
                    summary = "пример ответа",
                    value = """
                            {
                                "from": "Pizza",
                                "count": "2"
                            }
                            """
            ))
    })
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение товаров", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(
                            summary = "Пример ответа, продукты пришли",
                            value = """
                                    [
                                        {
                                            "id": 0,
                                            "productName": "Мясная",
                                            "basePrice": 499.0,
                                            "imageName": "0.webp",
                                            "productDescription": "Цыпленок, ветчина, пикантная пепперони, острая чоризо, моцарелла, фирменный томатный соус"
                                        },
                                        {
                                            "id": 1,
                                            "productName": "Мортаделла с песто",
                                            "basePrice": 549.0,
                                            "imageName": "1.webp",
                                            "productDescription": "Мортаделла, брынза, моцарелла, соус песто, фирменный томатный соус"
                                        }
                                    ]"""
                    ))
            }),
            @ApiResponse(responseCode = "400", description = "Возможные варианты, когда выбрасывается ошибка 400 " +
                    "\n 1. Не прошла валидация для полей ShowDTO;" +
                    "\n 2. 'count' может быть только в таких пределах 0 < count < 15 (в 1 запросе не больше 15 товаров)" +
                    "\n 3. 'from' может принимать значения только из enum ProductTypes" +
                    "\n 4. Сортировку можно делать только по полям класса ProductDTO, а единственный стороний доступный символ - '-' перед названием поля, для сортировки в порядке убывания")
    }
    )
    public ResponseEntity<List<ProductDTO>> showProducts(@RequestBody ShowDTO showDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            throw new ShowDTOValidationException(bindingResult);
        showDTOValidator.validate(showDTO,bindingResult);
        Pageable pageable = productService.getPageable(showDTO);
        List<ProductDTO> productList =  productService.findAllCertainProductsAndDisplayCertainCount(showDTO.getFrom(), pageable).stream().map(this::convertToProductDTO).toList();
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    @PostMapping("/product")
    public ResponseEntity<Object> showProduct(@RequestBody ShowProductDTO showProductDTO){
        Product product = productService.findById(showProductDTO.getId());
        if (Objects.equals(product.getProductType().getName(), ProductTypes.Pizza.name())){
            Object pizzaVariantDTOList = product.getProductVariants().stream().map(pizzaVariant -> modelMapper.map(pizzaVariant, PizzaVariantDTO.class)).toList();
            return new ResponseEntity<>(pizzaVariantDTOList,HttpStatus.OK);
        }
        return null;
    }
    public ProductDTO convertToProductDTO(Product product){
        return this.modelMapper.map(product, ProductDTO.class);
    }

    @ExceptionHandler(ShowDTOValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<Void> handleException(ShowDTOValidationException e){
        String k = e.getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("; "));
        //ClientResponse clientResponse = new ClientResponse(k, System.currentTimeMillis());
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CountException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<Void> handleException(CountException e){
        //ClientResponse clientResponse = new ClientResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SortException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<Void> handleException(SortException e){
        //ClientResponse clientResponse = new ClientResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<Void> handleException(HttpMessageNotReadableException e){
        //ClientResponse clientResponse = new ClientResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
    }

}
