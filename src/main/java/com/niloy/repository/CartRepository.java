package com.niloy.repository;

import com.niloy.modal.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    // Define any additional methods specific to Cart if needed
    // For example, you might want to find carts by user ID or status
    // List<Cart> findByUserId(Long userId);
    // List<Cart> findByStatus(String status);
}
