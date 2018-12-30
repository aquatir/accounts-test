package com.revolute.test.example.dto;

import com.revolute.test.example.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class AccountDto {
    private String number;
    private BigDecimal balance;

    public static AccountDto ofAccount(Account account) {
        return new AccountDto(account.getNumber(), account.getBalance());
    }
}
