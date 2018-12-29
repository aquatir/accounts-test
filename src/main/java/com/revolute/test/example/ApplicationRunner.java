package com.revolute.test.example;

import com.revolute.test.example.api.AccountAPI;
import com.revolute.test.example.db.Datasource;
import com.revolute.test.example.dto.TransferRequest;
import com.revolute.test.example.service.AccountService;
import com.revolute.test.example.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

import static spark.Spark.*;

@Slf4j
public class ApplicationRunner {

    private static AccountAPI accountAPI;

    public static void main(String[] args) throws SQLException {
        initializeApplication();
        initializeWeb();
    }

    /** Initialize database and inject all dependencies into *API classes */
    private static void initializeApplication() {
        var datasource = new Datasource();
        datasource.init("jdbc:h2:mem:test", "sa", "",
                "INIT=RUNSCRIPT FROM 'classpath:schema.sql'");
        var jsonMapper = new JsonMapper();
        var accService = new AccountService(datasource);

        accountAPI = new AccountAPI(accService, jsonMapper);
    }


    /** Initialize http endpoints */
    private static void initializeWeb() {

        port(8080);

        path("/api", () -> {
            get("/transfer", accountAPI::transfer);
        });
    }
}
