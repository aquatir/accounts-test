package com.revolute.test.example.repository;

import com.revolute.test.example.entity.Account;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AccountRepository {

    private final String SELECT_FOR_UPDATE_BY_ACCOUNT_NUMBER_PREP_ST = "select * from ACCOUNT for update" +
            "where number = ?";

    public Account findForUpdateByAccountNumber(Connection connection, String accountNumber) {

        try {
            var prepareSelect = connection.prepareStatement(SELECT_FOR_UPDATE_BY_ACCOUNT_NUMBER_PREP_ST);
            prepareSelect.setString(1, accountNumber);
            var resultSet = prepareSelect.executeQuery();

            return null;

        } catch (SQLException sqlException) {

            return null;
        }
    }

    public void saveAll(List<Account> accountFrom) {

    }
}
