package com.revolute.test.example;

import com.revolute.test.example.api.AccountAPI;
import com.revolute.test.example.db.Datasource;
import com.revolute.test.example.repository.AccountRepository;
import com.revolute.test.example.service.AccountService;
import com.revolute.test.example.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.SQLException;

import static spark.Spark.*;

@Slf4j
public class ApplicationRunner {

    private static Datasource datasource;
    private static AccountAPI accountAPI;

    public static void main(String[] args) throws SQLException {
        initializeApplication();
    }

    /** Initialize database and inject all dependencies into *API classes */
    public static void initializeApplication() {
        datasource = initializeDB();
        var jsonMapper = new JsonMapper();

        var accRepository = new AccountRepository();
        var accService = new AccountService(datasource, accRepository);

        accountAPI = new AccountAPI(accService, jsonMapper);

        log.info("API classes initialized");
        log.info("Starting web...");

        initializeWeb();
    }

    public static void tearDownApplication() {
        stop();
        datasource.dropSchema();
    }

    private static Datasource initializeDB() {
        var datasource = new Datasource();

        datasource.init("jdbc:h2:mem:test", "sa", "",
                "schema.sql");

        return datasource;
    }


    /** Initialize http endpoints */
    private static void initializeWeb() {

        port(8080);

        path("/api", () -> {
            post("/transfer", accountAPI::transfer);
        });

        after((request, response) -> response.type("application/json"));
    }
}
