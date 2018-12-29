package com.revolute.test.example.api;

import com.revolute.test.example.dto.TransferRequest;
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
        var transferRequest = jsonMapper.toObject(request.body(), TransferRequest.class);

        accountService.checkAndTransfer(transferRequest.getAccountFromNumber(),
                transferRequest.getAccountFromNumber(),
                transferRequest.getAmount());


        return response;
    }
}
