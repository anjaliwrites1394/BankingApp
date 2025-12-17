package com.example.banking_app.repository;

import com.example.banking_app.entity.Users;
import com.example.banking_app.entity.UsersBankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsersBankAccountRepository extends JpaRepository<UsersBankAccount, Long> {

    List<UsersBankAccount> findByUser(Users users);
}
