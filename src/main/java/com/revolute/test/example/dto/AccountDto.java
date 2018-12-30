package com.revolute.test.example.dto;

import com.revolute.test.example.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Getter
public class AccountDto {
    private final String number;
    private final BigDecimal balance;

    public static AccountDto ofAccount(Account account) {
        return new AccountDto(account.getNumber(), account.getBalance());
    }
}
