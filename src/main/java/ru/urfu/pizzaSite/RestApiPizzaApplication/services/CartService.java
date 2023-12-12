package ru.urfu.pizzaSite.RestApiPizzaApplication.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Cart;
import ru.urfu.pizzaSite.RestApiPizzaApplication.repositories.CartRepository;

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
}
