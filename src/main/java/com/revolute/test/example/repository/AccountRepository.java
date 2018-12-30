package com.revolute.test.example.repository;

import com.revolute.test.example.entity.Account;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Execute SQL queries. This class is not designed to manage transactions! It only manages statements.
 * Transaction management should happen in caller-class.
 */
@Slf4j
public class AccountRepository extends BaseRepository {

    private final String SELECT_BY_ACCOUNT_NUMBER = "select * from ACCOUNT where number = ?";
    private final String SELECT_FOR_UPDATE_BY_ACCOUNT_NUMBER = "select * from ACCOUNT " +
            "where number = ? for update";
    private final String UPDATE_BALANCE_BY_ACCOUNT_NUMBER = "UPDATE ACCOUNT SET BALANCE = ? " +
            "WHERE number = ?";

    public Optional<Account> findForUpdateByAccountNumber(Connection connection, String accountNumber) {

        PreparedStatement prepareSelect = null;

        try {
            prepareSelect = connection.prepareStatement(SELECT_FOR_UPDATE_BY_ACCOUNT_NUMBER);
            prepareSelect.setString(1, accountNumber);
            var resultSet = prepareSelect.executeQuery();

            return Account.ofSingleAccountResult(resultSet);

        } catch (SQLException sqlException) {
            log.error("Failed to execute findForUpdateByAccountNumber", sqlException);

            return Optional.empty();

        } finally {
            closePreparedStatement(prepareSelect);
        }
    }

    public void updateBalance(Connection connection, List<Account> accounts) {

        for (Account acc: accounts) {
            PreparedStatement prepareUpdate = null;

            try {
                prepareUpdate = connection.prepareStatement(UPDATE_BALANCE_BY_ACCOUNT_NUMBER);
                prepareUpdate.setBigDecimal(1, acc.getBalance());
                prepareUpdate.setString(2, acc.getNumber());

                prepareUpdate.execute();

            } catch (SQLException sqlException) {
                log.error("Failed to execute updateBalance", sqlException);
            } finally {
                closePreparedStatement(prepareUpdate);
            }
        }

    }

    public Optional<Account> findOneByNumber(Connection connection, String accountNumber) {
        PreparedStatement prepareSelect = null;

        try {
            prepareSelect = connection.prepareStatement(SELECT_BY_ACCOUNT_NUMBER);
            prepareSelect.setString(1, accountNumber);
            var resultSet = prepareSelect.executeQuery();

            return Account.ofSingleAccountResult(resultSet);

        } catch (SQLException sqlException) {
            log.error("Failed to execute findOneByNumber", sqlException);

            return Optional.empty();

        } finally {
            closePreparedStatement(prepareSelect);
        }
    }
}
