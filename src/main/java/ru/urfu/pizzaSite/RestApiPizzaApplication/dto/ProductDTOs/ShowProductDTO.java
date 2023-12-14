package ru.urfu.pizzaSite.RestApiPizzaApplication.dto.ProductDTOs;

import jakarta.validation.constraints.NotNull;

public class ShowProductDTO {
    @NotNull(message = "id продукта не может быть равно null")
    private Integer id;

    public ShowProductDTO(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
