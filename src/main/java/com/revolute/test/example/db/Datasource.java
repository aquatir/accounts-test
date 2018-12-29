package com.revolute.test.example.db;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Holds connection to database.
 *
 * Let's pretend that we live in a perfect world where DB connection is never getting closet for no apparent reason
 */
@Slf4j
@Getter
public class Datasource {

    private Connection connection;

    public void init(String jdbcUrl, String user, String password, String extraProperties) {
        var realJdbcUrl = jdbcUrl + Optional.ofNullable(extraProperties)
                .map(value -> ";" + value)
                .orElse("");

        try {
            connection = DriverManager.getConnection(jdbcUrl, user, password);
            log.info("Datasource initialized");
        } catch (SQLException exception) {
            log.error("Failed to initialize datasource", exception);
            throw new IllegalStateException("Initial connection and schema failed to get initialized");
        }
    }

    public void rollbackConnection(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException e) {
            log.error("Rollback failed!", e);
        }
    }
}
