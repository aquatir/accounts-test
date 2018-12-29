package com.revolute.test.example.api;

import com.revolute.test.example.service.AccountService;
import com.revolute.test.example.util.JsonMapper;
import lombok.RequiredArgsConstructor;
import spark.Request;
import spark.Response;

@RequiredArgsConstructor
public class AccountAPI {

    private final AccountService accountService;
    private final JsonMapper jsonMapper;

    public Response transfer(Request request, Response response) {
        return response;
    }
}
