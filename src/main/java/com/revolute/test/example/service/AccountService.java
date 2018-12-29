package com.revolute.test.example.service;

import com.revolute.test.example.db.Datasource;
import com.revolute.test.example.exception.InsufficientBalanceException;
import com.revolute.test.example.entity.Account;
import com.revolute.test.example.util.JsonMapper;
import lombok.RequiredArgsConstructor;
import spark.Request;
import spark.Response;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class AccountService {

    private final Datasource datasource;

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
