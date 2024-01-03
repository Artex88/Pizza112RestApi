package ru.urfu.pizzaSite.RestApiPizzaApplication.dto.ClientDTOs;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class ReviewDTO {

    @Column(name = "rating")
    @NotNull
    private Double rating;

    @Column(name = "text")
    @NotNull
    @NotEmpty
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
