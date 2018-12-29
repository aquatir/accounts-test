package com.revolute.test.example.db;

import com.zaxxer.hikari.HikariDataSource;
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
public class Datasource {

    private HikariDataSource datasource;

    public void init(String jdbcUrl, String user, String password, String extraProperties) {
        this.datasource = new HikariDataSource();

        var realJdbcUrl = jdbcUrl + Optional.ofNullable(extraProperties)
                .map(value -> ";" + value)
                .orElse("");

        datasource.setJdbcUrl(realJdbcUrl);
        datasource.setUsername(user);
        datasource.setPassword(password);
        datasource.setAutoCommit(false);
    }

    public Connection getConnection() throws SQLException {
        return this.datasource.getConnection();
    }

    public void rollbackConnection(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException e) {
            log.error("Rollback failed!", e);
        }
    }
}
