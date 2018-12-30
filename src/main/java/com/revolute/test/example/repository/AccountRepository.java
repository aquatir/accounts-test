package com.revolute.test.example.repository;

import com.revolute.test.example.entity.Account;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
public class AccountRepository {

    private final String SELECT_FOR_UPDATE_BY_ACCOUNT_NUMBER_PREP_ST = "select * from ACCOUNT " +
            "where number = ? for update";

    public Optional<Account> findForUpdateByAccountNumber(Connection connection, String accountNumber) {

        PreparedStatement prepareSelect = null;

        try {
            prepareSelect = connection.prepareStatement(SELECT_FOR_UPDATE_BY_ACCOUNT_NUMBER_PREP_ST);
            prepareSelect.setString(1, accountNumber);
            var resultSet = prepareSelect.executeQuery();

            return Account.ofSingleAccountResult(resultSet);

        } catch (SQLException sqlException) {
            log.error("Failed to execute findForUpdateByAccountNumber", sqlException);

            return Optional.empty();

        } finally {
            try {
                if (prepareSelect != null) {
                    prepareSelect.close();
                }
            } catch (SQLException e) {
                log.error("Failed to close prepared statement", e);
            }
        }
    }

    public void updateBalance(List<Account> accountFrom) {

    }
}
