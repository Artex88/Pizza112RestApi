package ru.urfu.pizzaSite.RestApiPizzaApplication.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.CartItem;
import ru.urfu.pizzaSite.RestApiPizzaApplication.repositories.CartItemRepository;

@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    @Autowired
    public CartItemService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional
    public void saveCartItem(CartItem cartItem){
        cartItemRepository.save(cartItem);
    }

}
