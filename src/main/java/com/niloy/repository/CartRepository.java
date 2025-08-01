package com.niloy.repository;

import com.niloy.modal.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

   Cart findByUserId(Long userId);
}
