package com.revolute.test.example.service;

import com.revolute.test.example.db.Datasource;
import com.revolute.test.example.repository.AccountRepository;
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
    public void checkAndTransferTest() throws SQLException {
        this.accountService.checkAndTransfer("A", "B", BigDecimal.ONE);
    }
}
