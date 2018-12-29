package com.revolute.test.example.service;

import com.revolute.test.example.db.Datasource;
import com.revolute.test.example.exception.InsufficientBalanceException;
import com.revolute.test.example.entity.Account;
import com.revolute.test.example.repository.AccountRepository;
import com.revolute.test.example.util.JsonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spark.Request;
import spark.Response;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final Datasource datasource;
    private final AccountRepository accountRepository;

    /** Transfer amount from accountFrom to accountTo.
     * @return AccountFrom with updated balance
     * @throws InsufficientBalanceException if accountFrom doesn't have required amount
     * @throws SQLException if for some reason transfer can not be executed */
    public Account checkAndTransfer(String accountFromNumber, String accountToNumber, BigDecimal amount)
            throws InsufficientBalanceException, SQLException {

        var connection = this.datasource.getConnection();

        try {
            connection.setAutoCommit(false);

            var accountFrom = accountRepository.findForUpdateByAccountNumber(connection, accountFromNumber);
            var accountTo = accountRepository.findForUpdateByAccountNumber(connection, accountToNumber);

            checkAndTransfer(accountFrom, accountTo, amount);
            this.accountRepository.saveAll(List.of(accountFrom, accountTo));

            connection.commit();

            return accountFrom;

        } catch (SQLException sqlException) {
            datasource.rollbackConnection(connection);
            throw sqlException;
        } catch (InsufficientBalanceException insufficientBalanceException) {
            datasource.rollbackConnection(connection);
            throw insufficientBalanceException;
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
