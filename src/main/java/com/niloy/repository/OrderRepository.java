package com.niloy.repository;

import com.niloy.modal.Order;
import com.niloy.modal.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUserId(Long userId);

    List<Order> findOrdersBySellerId(Long sellerId);
}
