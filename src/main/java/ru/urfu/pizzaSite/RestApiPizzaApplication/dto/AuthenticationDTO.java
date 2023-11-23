package ru.urfu.pizzaSite.RestApiPizzaApplication.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class AuthenticationDTO {
    @Column(name = "phone_number")
    @NotEmpty(message = "Номер не должен быть пустым")
    @Size(min = 11, max = 11 , message = "Номер должен содержать ровно 11 цифр")
    private String phoneNumber;

    @NotEmpty(message = "Пароль не должен быть пустым")
    @Size(min = 6, max = 6, message = "Пароль должен быть ровно 11 цифр")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
