package ru.urfu.pizzaSite.RestApiPizzaApplication.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class ClientDTO {

    @Column(name = "phone_number")
    @NotEmpty(message = "Номер не должен быть пустым")
    @Size(min = 11, max = 11 , message = "Номер должен содержать ровно 11 цифр")
    private String phoneNumber;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
