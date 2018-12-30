package com.revolute.test.example.db;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

@Slf4j
public class Datasource {

    private HikariDataSource datasource;

    public void init(String jdbcUrl, String user, String password, String initialSchema) {

        /* TODO: This will force all new connections to use the same extra properties.
           if schema is passed to every one of them -> each of connections will try to execute if and fail.
           This should be fixed */
        var realJdbcUrl = jdbcUrl + Optional.ofNullable(initialSchema)
                .map(value -> ";" + value)
                .orElse("");

        this.datasource = new HikariDataSource();
        datasource.setJdbcUrl(realJdbcUrl);
        datasource.setUsername(user);
        datasource.setPassword(password);
        datasource.setAutoCommit(false);
    }

    public Connection getConnection()  {
        try {
            return this.datasource.getConnection();
        } catch (SQLException e) {
            log.error("Could not get connection due to exception", e);
            return null;
        }
    }

    public void rollbackConnection(Connection connection) {
        try {
            if (connection != null) {
                log.info("Rolling back connection");
                connection.rollback();
            } else {
                log.error("Connection is NULL. Can not rollback");
            }
        } catch (SQLException e) {
            log.error("Rollback failed!", e);
        }
    }

    public void closeCollection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            } else {
                log.error("Connection is NULL. Can not be closed");
            }
        } catch (SQLException e) {
            log.error("Collection close failed!", e);
        }
    }
}
