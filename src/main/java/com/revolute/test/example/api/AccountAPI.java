package com.revolute.test.example.api;

import com.revolute.test.example.dto.AccountDto;
import com.revolute.test.example.dto.ExceptionResponse;
import com.revolute.test.example.dto.TransferRequest;
import com.revolute.test.example.exception.InsufficientBalanceException;
import com.revolute.test.example.service.AccountService;
import com.revolute.test.example.util.JsonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spark.Request;
import spark.Response;

import java.sql.SQLException;

@RequiredArgsConstructor
@Slf4j
public class AccountAPI {

    private final AccountService accountService;
    private final JsonMapper jsonMapper;

    /**
     * Transfer amount from one account to another <br>
     *
     * Takes {@link TransferRequest} and return {@link AccountDto} or {@link ExceptionResponse} if something goes wrong
     */
    public Response transfer(Request request, Response response) {
        var transferRequest = jsonMapper.toObject(request.body(), TransferRequest.class);

        try {
            var account = accountService.checkAndTransfer(transferRequest.getAccountFromNumber(),
                    transferRequest.getAccountFromNumber(),
                    transferRequest.getAmount());

            if (account == null) {
                addTransferFailedInfoToResponse(response);
                return response;
            }

            response.body(jsonMapper.toJson(AccountDto.ofAccount(account)));
            return response;

        } catch (InsufficientBalanceException insufficientBalanceException) {
            log.error("Failed to transfer money. Account " + transferRequest.getAccountFromNumber() + " does not have sufficient funds",
                    insufficientBalanceException);

            addTransferFailedInfoToResponse(response);
            return response;

        }
    }

    private void addTransferFailedInfoToResponse(Response response) {
        response.body(jsonMapper.toJson(ExceptionResponse.ofText("Failed to transfer")));
        response.status(500);
    }
}
