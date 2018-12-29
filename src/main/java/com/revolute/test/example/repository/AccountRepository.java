package com.revolute.test.example.repository;

import com.revolute.test.example.entity.Account;

import java.sql.Connection;
import java.util.List;

public class AccountRepository {

    public Account findForUpdateByAccountNumber(Connection connection, String accountNumber) {

        return null;
    }

    public void saveAll(List<Account> accountFrom) {

    }
}
