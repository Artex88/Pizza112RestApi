package ru.urfu.pizzaSite.RestApiPizzaApplication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Cart;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.CartItem;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Product;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.ProductVariant;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    @Modifying
    @Query("UPDATE CartItem SET quantity = quantity + 1 where productVariant = :productVariant and cart = :cart")
    void updateCartItemByQuantity(@Param("productVariant") ProductVariant productVariant, @Param("cart") Cart cart);
    @Modifying
    @Query("UPDATE CartItem SET quantity = quantity + 1 where product = :product and cart = :cart")
    void updateCartItemByQuantity(@Param("product") Product product, @Param("cart") Cart cart);

}
