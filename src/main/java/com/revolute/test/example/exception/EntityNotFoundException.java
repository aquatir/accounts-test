package com.revolute.test.example.exception;

import java.sql.SQLException;

public class EntityNotFoundException extends SQLException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
