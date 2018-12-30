package com.revolute.test.example.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
public class Account {

    private long id;

    /** business identifier of this account */
    private String number;
    private BigDecimal balance;

    public static Optional<Account> ofSingleAccountResult(ResultSet resultSet) throws SQLException {
        var accountFound = resultSet.next();
        if (!accountFound)
            return Optional.empty();
        else {
            var account = new Account();
            account.setId(resultSet.getLong("ID"));
            account.setNumber(resultSet.getString("NUMBER"));
            account.setBalance(resultSet.getBigDecimal("BALANCE"));

            return Optional.of(account);
        }
    }


    /** Add amount to current account balance */
    public BigDecimal addAmount(BigDecimal amount) {
        this.balance = this.balance.add(amount);
        return this.balance;
    }

    /** Subtract amount from current account balance */
    public BigDecimal subtractAmount(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
        return this.balance;
    }

    /** Return true if this account's balance has at least targetBalance amount on account */
    public boolean hasBalanceOfAtLeast(BigDecimal targetBalance) {
        return this.balance.compareTo(targetBalance) >= 0;
    }
}
