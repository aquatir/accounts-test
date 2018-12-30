package com.revolute.test.example.service;

import com.revolute.test.example.db.Datasource;
import com.revolute.test.example.exception.EntityNotFoundException;
import com.revolute.test.example.exception.InsufficientBalanceException;
import com.revolute.test.example.entity.Account;
import com.revolute.test.example.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final Datasource datasource;
    private final AccountRepository accountRepository;

    /** Transfer amount from accountFrom to accountTo. <br>
     *
     * @throws InsufficientBalanceException if accountFrom doesn't have required amount
     * @throws IllegalArgumentException if accountFrom == accountTo
     * @return AccountFrom with updated balance or NULL if SQL exception occurs while executing this method.
     * */
    public Account checkAndTransfer(String accountFromNumber, String accountToNumber, BigDecimal amount)
            throws InsufficientBalanceException, IllegalArgumentException {

        if (accountFromNumber.equals(accountToNumber)) {
            throw new IllegalArgumentException("FROM and TO accounts are the same");
        }

        Connection connection = null;

        try {

            connection = this.datasource.getConnection();

            var maybeAccountFrom = accountRepository.findForUpdateByAccountNumber(connection, accountFromNumber);
            var maybeAccountTo = accountRepository.findForUpdateByAccountNumber(connection, accountToNumber);

            var accountFrom = maybeAccountFrom.orElseThrow(() -> new EntityNotFoundException("Account with number " + accountFromNumber + " could not be found"));
            var accountTo = maybeAccountTo.orElseThrow(() -> new EntityNotFoundException("Account with number " + accountToNumber + " could not be found"));

            checkAndTransfer(accountFrom, accountTo, amount);
            this.accountRepository.updateBalance(List.of(accountFrom, accountTo));

            connection.commit();

            return accountFrom;

        } catch (SQLException sqlException) {
            datasource.rollbackConnection(connection);
            return null;
        } catch (InsufficientBalanceException insufficientBalanceException) {
            datasource.rollbackConnection(connection);
            throw insufficientBalanceException;
        } finally {
            datasource.closeCollection(connection);
        }
    }

    private void checkAndTransfer(Account from, Account to, BigDecimal amount) throws InsufficientBalanceException {

        if (!from.hasBalanceOfAtLeast(amount)) {
            throw new InsufficientBalanceException("Account " + from.getNumber() + " does not have sufficient funds");
        }

        transfer(from, to, amount);

    }

    private void transfer(Account from, Account to, BigDecimal amount) {
        from.subtractAmount(amount);
        to.addAmount(amount);
    }
}
