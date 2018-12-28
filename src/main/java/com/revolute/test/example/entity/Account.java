package com.revolute.test.example.entity;

import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
public class Account {

    private long id;
    private BigDecimal balance;

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
