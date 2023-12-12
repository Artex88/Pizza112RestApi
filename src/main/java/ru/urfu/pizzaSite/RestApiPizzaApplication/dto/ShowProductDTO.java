package ru.urfu.pizzaSite.RestApiPizzaApplication.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class ShowProductDTO {
    @NotNull
    @NotEmpty(message = "id товара не может быть пустым")
    private int productId;

    public ShowProductDTO(){

    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
