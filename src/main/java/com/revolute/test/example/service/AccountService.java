package com.revolute.test.example.service;

import com.revolute.test.example.InsufficientBalanceException;
import com.revolute.test.example.entity.Account;

import java.math.BigDecimal;

public class AccountService {

    public void checkAndTransfer(Account from, Account to, BigDecimal amount) throws InsufficientBalanceException {

        if (!from.hasBalanceOfAtLeast(amount)) {
            throw new InsufficientBalanceException();
        }

        transfer(from, to, amount);

    }


    public void transfer(Account from, Account to, BigDecimal amount) {
        from.subtractAmount(amount);
        to.addAmount(amount);
    }
}
