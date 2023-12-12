package ru.urfu.pizzaSite.RestApiPizzaApplication.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class ShowProductDTO {
    @NotNull
    @NotEmpty(message = "id товара не может быть пустым")
    private int id;

    public ShowProductDTO(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
