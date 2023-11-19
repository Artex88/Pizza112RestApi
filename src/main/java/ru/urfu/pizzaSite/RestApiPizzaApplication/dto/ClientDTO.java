package ru.urfu.pizzaSite.RestApiPizzaApplication.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class ClientDTO {

    @Column(name = "phone_number")
    @NotEmpty(message = "Пароль не должен быть пустым")
    @Size(min = 11, message = "Номер должен содержать 11 цифр")
    private String phoneNumber;

    @Column(name = "password")
    @NotEmpty(message = "Пароль не должен быть пустым")
    private String password;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
