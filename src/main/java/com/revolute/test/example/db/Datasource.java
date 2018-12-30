package com.revolute.test.example.db;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import spark.utils.IOUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * <p>Holds connection pool.</p>
 * <p>Allow getting/closing/rolling back connections</p>
 */
@Slf4j
public class Datasource {

    private HikariDataSource datasource;

    /** <p>Create Hikari connection pool and also execute initial script if any is provided </p>
     * <p>Throws unhandled RuntimeException if script should exist but can not be executed for any reason. Script
     * should be located on classpath </p>*/
    public void init(String jdbcUrl, String user, String password, String initialFilePath) {

        this.datasource = new HikariDataSource();
        datasource.setJdbcUrl(jdbcUrl);
        datasource.setUsername(user);
        datasource.setPassword(password);
        datasource.setAutoCommit(false);

        executeScriptOnDatabase(initialFilePath);

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
            log.error("Rollbacconnection, k failed!", e);
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
        executeScriptOnDatabase("dropSchema.sql");
    }

    /** Find script from classpath -> split into statements -> execute each statement */
    private void executeScriptOnDatabase(String initialFilePath) {
        try {

            var connection = this.getConnection();
            if (initialFilePath == null) {
                return;
            }
            if (connection == null) {
                throw new SQLException("Could not get connection from CP on call to executeScriptOnDatabase with " + initialFilePath);
            }

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
