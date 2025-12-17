package com.example.banking_app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class UsersBankAccountKey implements Serializable {

    @Column(name = "user_id")
    private Long userId;
    @Column(name = "bank_account_no")
    private Long bankAccountNo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsersBankAccountKey)) return false;
        UsersBankAccountKey that = (UsersBankAccountKey) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(bankAccountNo, that.bankAccountNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, bankAccountNo);
    }

}
