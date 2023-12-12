package ru.urfu.pizzaSite.RestApiPizzaApplication.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "cart")
public class Cart {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne()
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;

    @Column(name = "created_at")
    private LocalDateTime createdTime;

    @OneToMany(mappedBy = "cart")
    private Set<CartItem> cartItemSet;

    public Cart(){

    }

    public Cart(Client client, LocalDateTime createdTime, Set<CartItem> cartItemSet) {
        this.client = client;
        this.createdTime = createdTime;
        this.cartItemSet = cartItemSet;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public Set<CartItem> getCartItemSet() {
        return cartItemSet;
    }

    public void setCartItemSet(Set<CartItem> cartItemSet) {
        this.cartItemSet = cartItemSet;
    }
}
