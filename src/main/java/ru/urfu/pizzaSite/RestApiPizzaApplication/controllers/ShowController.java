package ru.urfu.pizzaSite.RestApiPizzaApplication.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.ClientDTOs.ClientReviewDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.ProductDTOs.*;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Client.ClientResponse;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Product.Product;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Review;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.Client.ReviewService;
import ru.urfu.pizzaSite.RestApiPizzaApplication.services.Product.ProductService;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.enums.ProductTypes;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.NotFoundException;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.SortException;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.validators.ShowDTOValidator;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.CountException;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.ValidationException;


import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/show")
@Tag(name = "Контроллер общей информации", description = "Методы для работы с общедоступными данными")
public class ShowController {

    private final ShowDTOValidator showDTOValidator;

    private final ProductService productService;

    private final ReviewService reviewService;

    private final ModelMapper modelMapper;

    @Autowired
    public ShowController(ShowDTOValidator showDTOValidator, ProductService productService, ReviewService reviewService, ModelMapper modelMapper) {
        this.showDTOValidator = showDTOValidator;
        this.productService = productService;
        this.reviewService = reviewService;
        this.modelMapper = modelMapper;
    }

    @PostMapping()
    @Operation(summary = "Получение определеного типа товара в определенном количестве + есть сортировка по полям товара (смотреть в ProductDTO.class, какие поля есть)(используется ShowDTO)")
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
    public ResponseEntity<List<ProductDTO>> showProducts(@RequestBody @Valid ShowDTO showDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            throw new ValidationException(bindingResult);
        showDTOValidator.validate(showDTO,bindingResult);
        Pageable pageable = productService.getPageable(showDTO);
        List<ProductDTO> productList =  productService.findAllCertainProductsAndDisplayCertainCount(showDTO.getFrom(), pageable).stream().map(this::convertToProductDTO).toList();
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    @PostMapping("/product")
    @Operation(summary = "Получение всех вариантов конкретного товара(используется ShowByIdDTO)")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Пример на получения всех вариантов мясной пиццы", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(
                    summary = "пример запроса",
                    value = """
                            {
                                "id": 0
                            }
                            """
            ))
    })
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение всех вариантов", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(
                            summary = "Пример ответа, варианты пришли",
                            value = """
                                    [
                                        {
                                            "name": "LTP",
                                            "price": 879.0,
                                            "weight": 700.0,
                                            "image": "0.webp"
                                        },
                                        {
                                            "name": "LP",
                                            "price": 879.0,
                                            "weight": 820.0,
                                            "image": "0.webp"
                                        },
                                        {
                                            "name": "MTP",
                                            "price": 749.0,
                                            "weight": 490.0,
                                            "image": "0.webp"
                                        },
                                        {
                                            "name": "STP",
                                            "price": 499.0,
                                            "weight": 290.0,
                                            "image": "0.webp"
                                        },
                                        {
                                            "name": "MP",
                                            "price": 749.0,
                                            "weight": 590.0,
                                            "image": "0.webp"
                                        },
                                        {
                                            "name": "SP",
                                            "price": 499.0,
                                            "weight": 390.0,
                                            "image": "0.webp"
                                        }
                                    ]"""
                    ))
            }),
            @ApiResponse(responseCode = "404", description = "Возможные варианты, когда выбрасывается ошибка 404 " +
                    "\n 1. Продукта с id, который передаеться, не существует.")
    }
    )
    public ResponseEntity<Object> showProduct(@RequestBody @Valid ShowByIdDTO showByIdDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            throw new ValidationException(bindingResult);
        Product product = productService.findById(showByIdDTO.getId());
        if (Objects.equals(product.getProductType().getName(), ProductTypes.Pizza.name())){
            Object pizzaVariantDTOList = product.getProductVariants()
                    .stream()
                    .map(pizzaVariant -> modelMapper.map(pizzaVariant, PizzaVariantDTO.class)).peek(pizzaVariantDTO -> pizzaVariantDTO.setImage(product.getImageName())).toList();
            return new ResponseEntity<>(pizzaVariantDTOList,HttpStatus.OK);
        }
        else if (Objects.equals(product.getProductType().getName(), ProductTypes.Snak.name())) {
            Object baseProductDTO = product.getProductVariants()
                    .stream()
                    .map(baseProductVariant -> modelMapper.map(baseProductVariant, BaseProductDTO.class)).peek(baseProductVariant -> baseProductVariant.setImage(product.getImageName())).toList();
            return new ResponseEntity<>(baseProductDTO,HttpStatus.OK);
        }
        else if (Objects.equals(product.getProductType().getName(), ProductTypes.Drink.name())){
            Object drinkProductDTO =  product.getProductVariants()
                    .stream()
                    .map(baseProductVariant -> modelMapper.map(baseProductVariant, DrinkDTO.class)).peek(baseProductVariant -> baseProductVariant.setImage(product.getImageName())).toList();
            return new ResponseEntity<>(drinkProductDTO,HttpStatus.OK);
        }
        return null;
    }


    @GetMapping("/reviews")
    @Operation(summary = "Получение 5-ти случаных отзывов из базы данных(используется ClientReviewDTO)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошёл успешно", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(
                            summary = "Пример ответа, где передаются все поля клиента",
                            value = """
                                        "rating": "3.5",
                                        "text": "на 3.5 норм",
                                        "authorName": "Петр",
                                        "authorAvatar": "default.webp",
                                        }
                                        ....... еще 4 подобных отзыва"""
                    ))
            })
    })
    public ResponseEntity<List<ClientReviewDTO>> getReviews(){
        List<ClientReviewDTO> reviewDTOList =  reviewService.getSomeReviews().stream().map(this::convertToReviewDTO).toList();
        return new ResponseEntity<>(reviewDTOList, HttpStatus.OK);
    }

    public ProductDTO convertToProductDTO(Product product){
        return this.modelMapper.map(product, ProductDTO.class);
    }
    public ClientReviewDTO convertToReviewDTO(Review review){
        return new ClientReviewDTO(review.getRating(), review.getText(), review.getClient().getName(), review.getClient().getImageName());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ResponseEntity<ClientResponse> handleException(NotFoundException e){
        ClientResponse clientResponse = new ClientResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(clientResponse, HttpStatus.NOT_FOUND);
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
