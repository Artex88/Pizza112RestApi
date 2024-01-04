package ru.urfu.pizzaSite.RestApiPizzaApplication.dto.ProductDTOs;

import jakarta.validation.constraints.NotNull;

public class ShowByIdDTO {
    @NotNull(message = "id не может быть равно null")
    private Integer id;

    public ShowByIdDTO(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
