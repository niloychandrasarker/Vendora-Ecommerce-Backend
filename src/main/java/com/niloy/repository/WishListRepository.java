package com.niloy.repository;

import com.niloy.modal.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListRepository extends JpaRepository<Wishlist, Long> {

    Wishlist findByUserId(Long userId);

}
