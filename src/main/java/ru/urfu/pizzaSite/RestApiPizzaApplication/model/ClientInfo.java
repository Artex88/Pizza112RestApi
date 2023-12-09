package ru.urfu.pizzaSite.RestApiPizzaApplication.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.Check;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "client_info")
public class ClientInfo {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "phone_number")
    @NotEmpty(message = "Поле номера телефона не должено быть пустым")
    @NotNull()
    @Size(min = 11, message = "Номер должен содержать 11 цифр")
    private String phoneNumber;

    @Column(name = "name")
    @Size(max = 32, message = "Имя может быть максимум 32 символа")
    @Pattern(regexp = "^[^0-9]*$", message = "В имени не должно быть цифр")
    private String name;

    @Column(name = "surname")
    @Size(max = 32, message = "Фамилия может быть максимум 32 символа")
    @Pattern(regexp = "^[^0-9]*$", message = "В фамилии не должно быть цифр")
    private String surname;

    @Column(name = "patronymic")
    @Size(max = 32, message = "Отчество может быть максимум 32 символа")
    @Pattern(regexp = "^[^0-9]*$", message = "В отчестве не должно быть цифр")
    private String patronymic;

    @Column(name = "photo_name")
    private String photoName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "email")
    @Email
    private String email;

    @OneToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;

    public ClientInfo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }
}
