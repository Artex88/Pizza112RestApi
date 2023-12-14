package ru.urfu.pizzaSite.RestApiPizzaApplication.dto.ClientDTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Schema(description = "Сущность для получения/обновления текстовых полей в ЛК пользователя. " +
        "В imageName приходит id.расширение картинки, которую можно открыть по пути serverDomain/images/Avatars/default.webp(ну или id.расширение, например 32.webp) (default.webp - дефолтный аватар пользователя)")
public class ClientInfoDTO {

    @Column(name = "name")
    @Size(max = 32, message = "Имя может быть максимум 32 символа")
    @Pattern(regexp = "^[^0-9]*$", message = "В имени не должно быть цифр")
    @Schema(description = "поле имени пользователя")
    private String name;

    @Column(name = "surname")
    @Size(max = 32, message = "Фамилия может быть максимум 32 символа")
    @Pattern(regexp = "^[^0-9]*$", message = "В фамилии не должно быть цифр")
    @Schema(description = "поле фамилии пользователя")
    private String surname;

    @Schema(description = "Поле, где хранится идентификатор аватара пользователя, доступный по ссылке serverDomain/images/Avatars/идентификатор ")
    private String imageName;

    @Column(name = "date_of_birth")
    @Schema(description = "дата рождения пользователя. Так как у нас не надо указывать год рождения, то нужно передавать в формате 1970-MM-dd, иначе ошибка")
    private LocalDate dateOfBirth;

    @Column(name = "email")
    @Email(message = "Поле почты должно иметь формат адреса электронной почты")
    @Schema(description = "поле электронной почты пользователя")
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
}
