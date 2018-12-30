package com.revolute.test.example.api;

import com.revolute.test.example.ApplicationRunner;
import com.revolute.test.example.dto.AccountDto;
import com.revolute.test.example.dto.ExceptionResponse;
import com.revolute.test.example.dto.TransferRequest;
import com.revolute.test.example.util.JsonMapper;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.utils.IOUtils;

import java.io.IOException;
import java.math.BigDecimal;
public class AccountAPITest {

    JsonMapper jsonMapper = new JsonMapper();

    @BeforeClass
    public static void testClassInit() {
        ApplicationRunner.initializeApplication();
    }


    /** TEST POST /api/transfer*/
    @Test
    public void api_checkAndTransfer_accountFromHasSufficientFunds_ExpectSuccess() throws IOException {

        var transferRequest = TransferRequest.of("A", "B", BigDecimal.ONE);
        var json = jsonMapper.toJson(transferRequest);

        var httpResponse = Request.Post("http://localhost:8080/api/transfer")
                .useExpectContinue()
                .bodyString(json, ContentType.APPLICATION_JSON)
                .execute().returnResponse();

        var status = httpResponse.getStatusLine().getStatusCode();
        Assert.assertEquals(200, status);

        var strResponse = IOUtils.toString(httpResponse.getEntity().getContent());
        var returnedAccount = jsonMapper.toObject(strResponse, AccountDto.class);

        Assert.assertEquals(BigDecimal.valueOf(1144,2), returnedAccount.getBalance());
    }

    @Test
    public void api_checkAndTransfer_accountFromDoesNotHaveSufficientFunds_Expect500() throws IOException {

        var transferRequest = TransferRequest.of("A", "B", BigDecimal.valueOf(1000));
        var json = jsonMapper.toJson(transferRequest);

        var httpResponse = Request.Post("http://localhost:8080/api/transfer")
                .useExpectContinue()
                .bodyString(json, ContentType.APPLICATION_JSON)
                .execute().returnResponse();

        var status = httpResponse.getStatusLine().getStatusCode();
        Assert.assertEquals(500, status);

        var strResponse = IOUtils.toString(httpResponse.getEntity().getContent());
        var exceptionResponse = jsonMapper.toObject(strResponse, ExceptionResponse.class);
        Assert.assertNotNull(exceptionResponse);
        Assert.assertNotEquals("", exceptionResponse.getText());
    }

    @Test
    public void api_checkAndTransfer_accountsAreTheSame_Expect400() throws IOException {

        var transferRequest = TransferRequest.of("A", "A", BigDecimal.ONE);
        var json = jsonMapper.toJson(transferRequest);

        var httpResponse = Request.Post("http://localhost:8080/api/transfer")
                .useExpectContinue()
                .bodyString(json, ContentType.APPLICATION_JSON)
                .execute().returnResponse();

        var status = httpResponse.getStatusLine().getStatusCode();
        Assert.assertEquals(400, status);

        var strResponse = IOUtils.toString(httpResponse.getEntity().getContent());
        var exceptionResponse = jsonMapper.toObject(strResponse, ExceptionResponse.class);
        Assert.assertNotNull(exceptionResponse);
        Assert.assertNotEquals("", exceptionResponse.getText());
    }
}