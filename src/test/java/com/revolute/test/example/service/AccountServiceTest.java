package com.revolute.test.example.service;

import com.revolute.test.example.db.Datasource;
import com.revolute.test.example.exception.InsufficientBalanceException;
import com.revolute.test.example.repository.AccountRepository;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.SQLException;

public class AccountServiceTest {

    private static AccountService accountService;

    @BeforeClass
    public static void testClassInit() {

        var datasource = new Datasource();
        datasource.init("jdbc:h2:mem:test", "sa", "",
                "INIT=RUNSCRIPT FROM 'classpath:schema.sql'");
        var accountRepository = new AccountRepository();

        accountService = new AccountService(datasource, accountRepository);
    }


    @Test
    public void checkAndTransfer_accountFromHasSufficientFunds_ExpectSuccess() throws SQLException {
        var accountAfterTransfer = accountService.checkAndTransfer("A", "B", BigDecimal.ONE);
        // TODO: Add check that balance has changed.
    }

    @Test
    public void checkAndTransfer_accountFromDoesNotExist_ExpectNoAccountFound() throws SQLException {
        var accountAfterUpdate = accountService.checkAndTransfer("Not existing account", "B", BigDecimal.ONE);
        Assert.assertNull(accountAfterUpdate);
    }

    @Test
    public void checkAndTransfer_accountToDoesNotExist_ExpectNoAccountFound() throws SQLException {
        var accountAfterUpdate = accountService.checkAndTransfer("A", "Not existing account", BigDecimal.ONE);
        Assert.assertNull(accountAfterUpdate);
    }

    @Test(expected = InsufficientBalanceException.class)
    public void checkAndTransfer_accountFromDoesNotHaveSufficientFunds_ExpectInsufficientBalanceException() throws SQLException {
        accountService.checkAndTransfer("A", "B", BigDecimal.valueOf(1000));
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkAndTransfer_AccountsAreTheSame_ExpectIllegalArgumentException() throws SQLException {
        accountService.checkAndTransfer("A", "A", BigDecimal.ONE);
    }
}
