package ru.urfu.pizzaSite.RestApiPizzaApplication.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "client")
public class Client {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "phone_number")
    @NotNull
    @NotEmpty(message = "Пароль не должен быть пустым")
    @Size(min = 11, message = "Номер должен содержать 11 цифр")
    private String phoneNumber;

    @Column(name = "password")
    private String password;

    @Column(name = "last_register_request_time")
    private LocalDateTime lastRegisterTime;

    @Column(name = "last_login_request_time")
    private LocalDateTime lastLoginTime;

    @Column(name = "login_attempts")
    private int login_attempts;

    @OneToOne(mappedBy = "client", cascade=CascadeType.ALL)
    private ClientInfo client_info;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private Set<Cart> cartSet;

    public Client() {
    }

    public Client(String phoneNumber, String password, LocalDateTime lastRegisterTime, LocalDateTime lastLoginTime) {
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.lastRegisterTime = lastRegisterTime;
        this.lastLoginTime = lastLoginTime;
    }

    public LocalDateTime getLastRegisterTime() {
        return lastRegisterTime;
    }

    public void setLastRegisterTime(LocalDateTime lastRegisterTime) {
        this.lastRegisterTime = lastRegisterTime;
    }

    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ClientInfo getClient_info() {
        return client_info;
    }

    public void setClient_info(ClientInfo client_info) {
        this.client_info = client_info;
    }

    public int getLogin_attempts() {
        return login_attempts;
    }

    public void setLogin_attempts(int login_attempts) {
        this.login_attempts = login_attempts;
    }

    public Set<Cart> getCartSet() {
        return cartSet;
    }

    public void setCartSet(Set<Cart> cartSet) {
        this.cartSet = cartSet;
    }
}
