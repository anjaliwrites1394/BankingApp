package com.example.banking_app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

    @Entity
    @Table(name = "UsersBankAccount")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class UsersBankAccount {

        @EmbeddedId
        private UsersBankAccountKey id;

        @ManyToOne
        @MapsId("userId")
        @JoinColumn(name = "user_id")
        private Users user;

        @ManyToOne
        @MapsId("bankAccountNo")
        @JoinColumn(name = "bank_account_no")
        private BankAccount bankAccount;

}
