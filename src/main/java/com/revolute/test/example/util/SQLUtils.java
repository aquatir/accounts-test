package com.revolute.test.example.util;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * Holds {@link Connection}. Uses static init block to get initial connection.
 *
 * TODO: This class should not exist as static utils class and should be substituted for injected database
 */
@Slf4j
public class SQLUtils {

    private static Connection connection;

    static {
        try {
            connection = initializeConnectionAndSchema();
            log.info("Initialized connection successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException("Initial connection and schema failed to get initialized");
        }
    }

    public static Connection getConnection() throws SQLException {
        if (connection != null) {
            return connection;
        } else {
            return initializeConnection();
        }
    }

    private static Connection initializeConnection() throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:h2:mem:test",
                "sa", "");
        connection.setAutoCommit(false);
        return connection;
    }

    private static Connection initializeConnectionAndSchema() throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:h2:mem:test;INIT=RUNSCRIPT FROM 'classpath:schema.sql'",
                "sa", "");
        connection.setAutoCommit(false);
        log.info("Schema run successfully over database");
        return connection;
    }
}
