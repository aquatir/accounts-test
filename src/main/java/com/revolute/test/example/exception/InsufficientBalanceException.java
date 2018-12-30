package com.revolute.test.example.exception;

/** Thrown when account does not have sufficient fund available*/
public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
