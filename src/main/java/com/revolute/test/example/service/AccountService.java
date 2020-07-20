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
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final Datasource datasource;
    private final AccountRepository accountRepository;

    /**
     * <p>Transfer amount from accountFrom to accountTo as a single transaction.</p>
     *
     * <p> This uses pessimistic select for update lock when updating accounts' balances. The queries used here utilize
     * select->check->update pattern which may work incorrectly due to phantom reads, thus either select for update OR
     * serializable transaction isolation level is required (With MVCC it can be optimistically locked).
     * The former approach is used here as it is easier to implement. <p>
     *
     * @return Optional.of(AccountFrom) with updated balance or Optional.empty if SQL exception occurs while executing this method.
     * @throws InsufficientBalanceException if accountFrom doesn't have required amount
     * @throws IllegalArgumentException     if accountFrom == accountTo
     */
    public Optional<Account> checkAndTransfer(String accountFromNumber, String accountToNumber, BigDecimal amount)
            throws InsufficientBalanceException, IllegalArgumentException {

        if (accountFromNumber.equals(accountToNumber)) {
            throw new IllegalArgumentException("FROM and TO accounts are the same");
        }

        Connection connection = null;

        try {

            connection = this.datasource.getConnection();
            if (connection == null) {
                throw new SQLException("Failed to get connection from CP on call to checkAndTransfer");
            }

            /** This is a trap! I warned you! */
            var accountFrom = accountRepository.findForUpdateByAccountNumber(connection, accountFromNumber)
                    .orElseThrow(() -> new EntityNotFoundException("Account with number " + accountFromNumber + " could not be found"));
            var accountTo = accountRepository.findForUpdateByAccountNumber(connection, accountToNumber)
                    .orElseThrow(() -> new EntityNotFoundException("Account with number " + accountToNumber + " could not be found"));

            checkAndTransfer(accountFrom, accountTo, amount);
            this.accountRepository.updateBalanceOnAll(connection, List.of(accountFrom, accountTo));

            connection.commit();

            return Optional.of(accountFrom);

        } catch (SQLException sqlException) {
            datasource.rollbackConnection(connection);
            return Optional.empty();

        } catch (InsufficientBalanceException insufficientBalanceException) {
            datasource.rollbackConnection(connection);
            throw insufficientBalanceException;
        } finally {
            datasource.closeCollection(connection);
        }
    }

    private void checkAndTransfer(Account from, Account to, BigDecimal amount) throws InsufficientBalanceException {

        /* In real world account sometimes DOES have negative balance (e.g. overdraft account, credint card, etc),
        * so this requirement maybe have come out of this air... */
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
