package com.bsd.moneymanager.moneyaccountsmicroservice.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@EqualsAndHashCode
@Getter
public class ExpenseDetails {
    private final long id;
    private final LocalDateTime createdAt;
    private final BigDecimal amount;
    private final String category;
}