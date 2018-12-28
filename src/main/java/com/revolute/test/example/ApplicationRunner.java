package com.revolute.test.example;

import static spark.Spark.*;

public class ApplicationRunner {
    public static void main(String[] args) {

        port(8080);

        path("/api", () -> {
            get("/hello", (req, res)->"Hello, world");
        });

    }
}
