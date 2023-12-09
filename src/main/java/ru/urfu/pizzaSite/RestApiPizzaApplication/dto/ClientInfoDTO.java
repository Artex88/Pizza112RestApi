package ru.urfu.pizzaSite.RestApiPizzaApplication.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

public class ClientInfoDTO {

    @Column(name = "name")
    @Size(max = 32, message = "Имя может быть максимум 32 символа")
    @Pattern(regexp = "^[^0-9]*$", message = "В имени не должно быть цифр")
    private String name;

    @Column(name = "phone_number")
    @Size(min = 11, message = "Номер должен содержать 11 цифр")
    private String phoneNumber;

    @Column(name = "surname")
    @Size(max = 32, message = "Фамилия может быть максимум 32 символа")
    @Pattern(regexp = "^[^0-9]*$", message = "В фамилии не должно быть цифр")
    private String surname;

    @Column(name = "patronymic")
    @Size(max = 32, message = "Отчество может быть максимум 32 символа")
    @Pattern(regexp = "^[^0-9]*$", message = "В отчестве не должно быть цифр")
    private String patronymic;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "email")
    @Email(message = "Поле почты должно иметь формат адреса электронной почты")
    private String email;

    public ClientInfoDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
