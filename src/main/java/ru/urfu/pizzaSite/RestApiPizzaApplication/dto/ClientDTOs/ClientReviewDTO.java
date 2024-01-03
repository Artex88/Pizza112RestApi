package ru.urfu.pizzaSite.RestApiPizzaApplication.dto.ClientDTOs;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class ClientReviewDTO {

    @Column(name = "rating")
    @NotNull
    private Double rating;

    @Column(name = "text")
    @NotNull
    @NotEmpty
    private String text;

    private String authorName;

    private String authorAvatar;

    public ClientReviewDTO(Double rating, String text, String authorName, String authorAvatar) {
        this.rating = rating;
        this.text = text;
        this.authorName = authorName;
        this.authorAvatar = authorAvatar;
    }

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

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorAvatar() {
        return authorAvatar;
    }

    public void setAuthorAvatar(String authorAvatar) {
        this.authorAvatar = authorAvatar;
    }
}
