package com.revolute.test.example;

import com.revolute.test.example.dto.TransferRequest;
import com.revolute.test.example.util.JsonUtils;
import com.revolute.test.example.util.SQLUtils;
import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.SQLException;

import static java.sql.DriverManager.getConnection;
import static spark.Spark.*;

@Slf4j
public class ApplicationRunner {

    public static void main(String[] args) throws SQLException {
        initializeApplication(args);
    }

    public static void initializeApplication(String[] args) {

        port(8080);

        path("/api", () -> {
            get("/transfer", (req, res) -> {
                var transferRequest = JsonUtils.toObject(req.body(), TransferRequest.class);


                return "keks";
            });
        });
    }
}
