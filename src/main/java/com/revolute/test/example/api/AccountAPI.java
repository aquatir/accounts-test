package com.revolute.test.example.api;

import com.revolute.test.example.dto.AccountDto;
import com.revolute.test.example.dto.ExceptionResponse;
import com.revolute.test.example.dto.TransferRequest;
import com.revolute.test.example.exception.InsufficientBalanceException;
import com.revolute.test.example.service.AccountService;
import com.revolute.test.example.util.JsonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
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
     * Takes {@link TransferRequest} and return as JSON either {@link AccountDto} or {@link ExceptionResponse} if something goes wrong. <br>
     *
     * In real world this should create a unique 'transaction' and probably be an idempotent PUT request in order to
     * guarantee exactly-once semantics for this transaction.
     */
    public String transfer(Request request, Response response) {
        var transferRequest = jsonMapper.toObject(request.body(), TransferRequest.class);

        try {
            var account = accountService.checkAndTransfer(transferRequest.getAccountFromNumber(),
                    transferRequest.getAccountToNumber(),
                    transferRequest.getAmount());

            return account.map(account1 -> jsonMapper.toJson(AccountDto.ofAccount(account1)))
                    .orElse(transferFailureMessage(response, 500));

        } catch (InsufficientBalanceException insufficientBalanceException) {
            log.error("Failed to transfer money. Account " + transferRequest.getAccountFromNumber() + " does not have sufficient funds",
                    insufficientBalanceException);

            return transferFailureMessage(response, 500);

        } catch (IllegalArgumentException illegalArgumentException) {
            log.warn("Can not transfer money. Accounts are the same");

            response.status(400);
            return jsonMapper.toJson(ExceptionResponse.ofText("FROM and TO accounts are the same"));
        }
    }

    private String transferFailureMessage(Response response, int statusCode) {
        response.status(statusCode);
        return jsonMapper.toJson(ExceptionResponse.ofText("Failed to transfer"));
    }
}
