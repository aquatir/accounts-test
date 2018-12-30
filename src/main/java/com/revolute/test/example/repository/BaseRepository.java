package com.revolute.test.example.repository;

import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Provide basic functionality for working with statements which can be used in all repositories
 */
@Slf4j
public abstract class BaseRepository {
    public void closePreparedStatement(PreparedStatement preparedStatement) {
        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (SQLException e) {
            log.error("Failed to close prepared statement", e);
        }
    }
}
