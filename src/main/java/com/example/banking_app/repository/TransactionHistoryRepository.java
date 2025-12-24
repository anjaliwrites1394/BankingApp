package com.example.banking_app.repository;

import com.example.banking_app.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionHistoryRepository extends JpaRepository<Transaction, Long> {
}
