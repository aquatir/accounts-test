package com.revolute.test.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TransferRequest {
    private Long accountFromNumber;
    private Long accountToNumber;
    private BigDecimal amount;
}
