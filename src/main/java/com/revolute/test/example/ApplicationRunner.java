package com.revolute.test.example;

import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;

import java.sql.SQLException;

import static spark.Spark.*;

@Slf4j
public class ApplicationRunner {

    public static void main(String[] args) throws SQLException {

        initializeDb();
        initializeApplication(args);
    }

    private static void initializeDb() throws SQLException {
        var h2Server = Server.createTcpServer().start();

        log.info("Started h2 with url: " + h2Server.getURL());
    }

    public static void initializeApplication(String[] args) {

        port(8080);

        path("/api", () -> {
            get("/hello", (req, res) -> "Hello, world");
        });
    }
}
