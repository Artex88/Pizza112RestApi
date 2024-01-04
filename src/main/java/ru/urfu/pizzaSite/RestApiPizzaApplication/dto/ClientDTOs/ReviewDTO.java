package ru.urfu.pizzaSite.RestApiPizzaApplication.dto.ClientDTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
@Schema(description = "DTO, которое нужно отправить для добавления отзыва в базу")
public class ReviewDTO {

    @Column(name = "rating")
    @NotNull
    @Schema(description = "поле рейтинга; доступен рейтинг от 1 до 5 со значеними типа 1.5 или 3.5")
    private Double rating;

    @Column(name = "text")
    @NotNull
    @NotEmpty
    @Schema(description = "поле текста; ограничение 255 символов")
    private String text;

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
