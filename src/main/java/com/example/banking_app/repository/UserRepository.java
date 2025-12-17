package com.example.banking_app.repository;

import com.example.banking_app.entity.BankAccount;
import com.example.banking_app.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    Users findByUsername(String username);
}
