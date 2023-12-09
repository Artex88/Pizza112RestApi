package ru.urfu.pizzaSite.RestApiPizzaApplication.dto;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.enums.ProductTypes;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.interfaces.EnumValidator;

public class ShowDTO {
    @NotNull(message = "Поле типа товара не может быть null")
    @NotEmpty(message = "Поле типа товара не может быть пустым")
    @Size(max = 64, message = "Название типа продукта может быть максимум 64 символа")
    @EnumValidator(enumClazz = ProductTypes.class, message = "Неправильно указан тип товара")
    private ProductTypes from;
    @NotNull
    @NotEmpty
    @Digits(integer = 2, fraction = 0, message = "Максимальное количество товаров, предоставляемых за один запрос - 15")
    private int count;
    @Size(max = 32, message = "Слишком большая строка")
    private String sort;

    public ShowDTO() {
    }

    public ProductTypes getFrom() {
        return from;
    }

    public void setFrom(ProductTypes from) {
        this.from = from;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
