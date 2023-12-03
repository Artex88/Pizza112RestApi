package ru.urfu.pizzaSite.RestApiPizzaApplication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.repository.query.Param;

@Schema(description = "Сущность, которую нужно передать для отправки сообщения")
public class ClientDTO {

    @Column(name = "phone_number")
    @NotNull
    @NotEmpty(message = "The number must not be empty")
    @Size(min = 11, max = 11 , message = "The number must contain exactly 11 digits")
    @Schema(description = "поле номера телефона")
    private String phoneNumber;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
