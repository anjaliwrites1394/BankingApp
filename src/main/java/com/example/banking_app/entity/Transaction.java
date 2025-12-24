package com.example.banking_app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name  = "Transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long transactionId;

    @Column(nullable = false)
    Long bankAccountNo;

    @Column(nullable = false)
    String transactionType;

    @Column(nullable = false)
    Double amount;

    @CreationTimestamp
    @Column(updatable = false)
    LocalDateTime createdTimeStamp;

}
