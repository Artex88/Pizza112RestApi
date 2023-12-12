package ru.urfu.pizzaSite.RestApiPizzaApplication.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.CartItemAddDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.*;
import ru.urfu.pizzaSite.RestApiPizzaApplication.repositories.CartItemRepository;

import java.util.Objects;

@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    @Autowired
    public CartItemService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional
    public void save(CartItem cartItem){
        cartItemRepository.save(cartItem);
    }

    @Transactional
    public void increaseQuantityByOneWithProductVariant(ProductVariant productVariant, Cart cart){
        cartItemRepository.updateCartItemByQuantity(productVariant, cart);
    }

    @Transactional
    public void increaseQuantityByOneWithoutProductVariant(Product product, Cart cart){
        cartItemRepository.updateCartItemByQuantity(product, cart);
    }

    @Transactional
    public void addNewCartItem(CartItemAddDTO cartItemAddDTO, Client client, Product product, ProductVariant productVariant) {
        CartItem cartItem = new CartItem(cartItemAddDTO.getQuantity(), client.getCart(), product, productVariant);
        this.save(cartItem);
        client.getCart().getCartItemSet().add(cartItem);
    }

    public boolean cartItemExists(Cart cart, Product product, ProductVariant productVariant) {
        return cart.getCartItemSet().stream().anyMatch(cartItem ->
                Objects.equals(cartItem.getProduct(), product) &&
                        Objects.equals(cartItem.getProductVariant(), productVariant));
    }
@Transactional
    public void updateCartItemByProductVariant(ProductVariant productVariant, CartItemAddDTO cartItemAddDTO, Product product, Client client) {
        if (this.cartItemExists(client.getCart(), product, productVariant))
            this.increaseQuantityByOneWithProductVariant(productVariant, client.getCart());
        else
            this.addNewCartItem(cartItemAddDTO, client, product,productVariant);
    }

    @Transactional
    public void updateCartItemByProduct(CartItemAddDTO cartItemAddDTO, Client client, Product product) {
        if (this.cartItemExists(client.getCart(), product, null)){
            this.increaseQuantityByOneWithoutProductVariant(product, client.getCart());
        }
        else
            this.addNewCartItem(cartItemAddDTO, client, product,null);
    }

}
