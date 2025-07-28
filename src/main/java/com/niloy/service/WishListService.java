package com.niloy.service;

import com.niloy.modal.Product;
import com.niloy.modal.User;
import com.niloy.modal.Wishlist;

public interface WishListService {
    Wishlist createWishlist(User user);
    Wishlist getWishlistByUserId(User user);
    Wishlist addProductToWishlist(User user, Product product);
}
