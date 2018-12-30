package com.revolute.test.example.db;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import spark.utils.IOUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
public class Datasource {

    private HikariDataSource datasource;

    public void init(String jdbcUrl, String user, String password, String initialFilePath) {

        this.datasource = new HikariDataSource();
        datasource.setJdbcUrl(jdbcUrl);
        datasource.setUsername(user);
        datasource.setPassword(password);
        datasource.setAutoCommit(false);

        var connection = this.getConnection();
        executeScriptOnDatabase(connection, initialFilePath);

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

    public void dropSchema() {
        var connection = this.getConnection();
        executeScriptOnDatabase(connection, "dropSchema.sql");
    }

    private void executeScriptOnDatabase(Connection connection, String initialFilePath) {
        try {
            var initialFileInputStream = this.getClass().getClassLoader()
                    .getResourceAsStream(initialFilePath);
            var scripts = IOUtils.toString(initialFileInputStream).split(";");

            Statement statement = connection.createStatement();

            for (String script : scripts) {
                if (!script.equals("\n"));
                statement.execute(script);
            }

            statement.close();
            connection.commit();
            connection.close();
        } catch (SQLException e) {
            log.error("Could not execute initial schema script", e);
            throw new RuntimeException();
        } catch (IOException e) {
            log.error("Could not find or open file: " + initialFilePath, e);
            throw new RuntimeException();
        }
    }
}
