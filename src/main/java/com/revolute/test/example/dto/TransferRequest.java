package com.revolute.test.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Getter
public class TransferRequest {
    private String accountFromNumber;
    private String accountToNumber;
    private BigDecimal amount;
}
