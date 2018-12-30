package com.revolute.test.example.service;

import com.revolute.test.example.db.Datasource;
import com.revolute.test.example.exception.InsufficientBalanceException;
import com.revolute.test.example.repository.AccountRepository;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.SQLException;

public class AccountServiceTest {

    private static Datasource datasource;
    private static AccountService accountService;
    private static AccountRepository accountRepository;

    @BeforeClass
    public static void testClassInit() {


        datasource = new Datasource();
        datasource.init("jdbc:h2:mem:test", "sa", "",
                "schema.sql");

        accountRepository = new AccountRepository();
        accountService = new AccountService(datasource, accountRepository);
    }

    @AfterClass
    public static void testClassTeardown() {
        datasource.dropSchema();
    }


    @Test
    public void checkAndTransfer_accountFromHasSufficientFunds_ExpectSuccess() throws SQLException {
        var accountAfterTransfer = accountService.checkAndTransfer("A", "B", BigDecimal.ONE);
        Assert.assertEquals(BigDecimal.valueOf(1144, 2), accountAfterTransfer.get().getBalance());

        var connection = datasource.getConnection();
        var maybeAcc1 = accountRepository.findOneByNumber(connection, "A");
        var maybeAcc2 = accountRepository.findOneByNumber(connection, "B");
        connection.commit();

        var acc1 = maybeAcc1.orElseThrow(() -> new AssertionError("No account A can be found in test"));
        var acc2 = maybeAcc2.orElseThrow(() -> new AssertionError("No account B can be found in test"));

        Assert.assertEquals(BigDecimal.valueOf(1144, 2), acc1.getBalance());
        Assert.assertEquals(BigDecimal.valueOf(4544, 2), acc2.getBalance());

    }

    @Test
    public void checkAndTransfer_accountFromDoesNotExist_ExpectNoAccountFound() throws SQLException {
        var accountAfterUpdate = accountService.checkAndTransfer("Not existing account", "B", BigDecimal.ONE);
        Assert.assertFalse(accountAfterUpdate.isPresent());
    }

    @Test
    public void checkAndTransfer_accountToDoesNotExist_ExpectNoAccountFound() throws SQLException {
        var accountAfterUpdate = accountService.checkAndTransfer("A", "Not existing account", BigDecimal.ONE);
        Assert.assertFalse(accountAfterUpdate.isPresent());
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
