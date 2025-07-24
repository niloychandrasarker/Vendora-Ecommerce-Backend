package com.niloy.service;

import com.niloy.modal.Cart;
import com.niloy.modal.CartItem;
import com.niloy.modal.Product;
import com.niloy.modal.User;

public interface CartService {
    public CartItem addCartItem(
            User user,
            Product product,
            String size,
            int quantity
    );
    public Cart findUserCart(User user);
}
