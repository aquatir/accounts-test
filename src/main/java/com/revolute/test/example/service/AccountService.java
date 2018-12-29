package com.revolute.test.example.service;

import com.revolute.test.example.db.Datasource;
import com.revolute.test.example.exception.InsufficientBalanceException;
import com.revolute.test.example.entity.Account;
import com.revolute.test.example.util.JsonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spark.Request;
import spark.Response;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final Datasource datasource;
    private final AccountRepository accountRepository;

    public void checkAndTransfer(String accountFromNumber, String accountToNumber, BigDecimal amount) throws InsufficientBalanceException {

        var connection = this.datasource.getConnection();


        // TODO: Refactor to connecton pool
        // Start transaction
        // Get both accounts blocking with for..update (maybe)
        // Update both
        // Save both
        // Close transaction

        connection.beginRequest();

        var accountFrom = accountRepository.findForUpdateByAccountNumber(accountFromNumber);
        var accountTo = accountRepository.findForUpdateByAccountNumber(accountToNumber);

        try {
            checkAndTransfer(accountFrom, accountTo, amount);
        } catch (InsufficientBalanceException ex) {
            log.error("Account " + accountFrom + "has no funds!");
        } finally {
            connection.endRequest();
        }



    }

    private void checkAndTransfer(Account from, Account to, BigDecimal amount) throws InsufficientBalanceException {

        if (!from.hasBalanceOfAtLeast(amount)) {
            throw new InsufficientBalanceException();
        }

        transfer(from, to, amount);

    }

    private void transfer(Account from, Account to, BigDecimal amount) {
        from.subtractAmount(amount);
        to.addAmount(amount);
    }
}
