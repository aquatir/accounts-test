package com.revolute.test.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor(staticName = "of")
@Getter
public class TransferRequest {
    private final String accountFromNumber;
    private final String accountToNumber;
    private final BigDecimal amount;
}
