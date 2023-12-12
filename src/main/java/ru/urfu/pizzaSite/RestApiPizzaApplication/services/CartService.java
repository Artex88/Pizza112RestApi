package ru.urfu.pizzaSite.RestApiPizzaApplication.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Cart;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Client;
import ru.urfu.pizzaSite.RestApiPizzaApplication.repositories.CartRepository;

import java.time.LocalDateTime;

@Service
public class CartService {

    private final CartRepository cartRepository;
    @Autowired
    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public void save(Cart cart){
        cartRepository.save(cart);
    }

    public void createCart(Client client) {
        Cart cart = new Cart(client, LocalDateTime.now(), null);
        this.save(cart);
        client.setCart(cart);
    }
}
