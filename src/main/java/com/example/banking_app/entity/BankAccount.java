package com.example.banking_app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
    @Table(name = "BankAccount")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class BankAccount {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long bankAccountNo;

        @OneToMany(mappedBy = "bankAccount", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<UsersBankAccount> usersBankAccount= new ArrayList<>();
        @Column(nullable = false)
        private Double currentBal;
        @CreationTimestamp
        private LocalDateTime creationTime;
        @UpdateTimestamp
        private LocalDateTime updatedTime;
        @Column(nullable = false)
        private String accountType;
}
