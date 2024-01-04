package ru.urfu.pizzaSite.RestApiPizzaApplication.dto.ProductDTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
@Schema(description = "ID любой сущности по необоходимости")
public class ShowByIdDTO {
    @NotNull(message = "id не может быть равно null")
    @Schema(description = "поле id")
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
