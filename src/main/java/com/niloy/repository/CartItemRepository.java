package com.niloy.repository;

import com.niloy.modal.Cart;
import com.niloy.modal.CartItem;
import com.niloy.modal.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartAndProductAndSize(Cart cart, Product product, String size);

}
