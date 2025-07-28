package com.niloy.service;

import com.niloy.modal.Order;
import com.niloy.modal.Seller;
import com.niloy.modal.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction createTransaction(Order order);
    List<Transaction> getTransactionsBySellerId(Seller seller);
    List<Transaction> getAllTransactions();
}
