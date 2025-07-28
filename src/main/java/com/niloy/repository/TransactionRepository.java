package com.niloy.repository;

import com.niloy.modal.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findBySellerId(Long sellerId);

    // Additional query methods can be defined here if needed
}
