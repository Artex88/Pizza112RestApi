package ru.urfu.pizzaSite.RestApiPizzaApplication.model.Client;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Review;

import java.time.LocalDate;
import java.util.List;

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

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "email")
    @Email
    private String email;

    @Column(name = "tg_token")
    private String tgToken;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "is_notifications_on")
    private boolean isNotificationsOn;

    @OneToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;

    @OneToMany(mappedBy = "client")
    private List<Review> reviewList;

    public ClientInfo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public boolean isNotificationsOn() {
        return isNotificationsOn;
    }

    public void setNotificationsOn(boolean notificationsOn) {
        isNotificationsOn = notificationsOn;
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

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String photoName) {
        this.imageName = photoName;
    }

    public List<Review> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    public String getTgToken() {
        return tgToken;
    }

    public void setTgToken(String tgToken) {
        this.tgToken = tgToken;
    }
}
