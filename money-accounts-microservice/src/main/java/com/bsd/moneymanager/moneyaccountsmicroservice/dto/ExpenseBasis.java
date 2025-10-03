package com.bsd.moneymanager.moneyaccountsmicroservice.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@EqualsAndHashCode
@Getter
public class ExpenseBasis {
    private final long moneyAccountId;
    private final BigDecimal amount;
    private final String category;
}