package com.bsd.moneymanager.moneyaccountsmicroservice.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@EqualsAndHashCode
@Getter
public class DepositDetails {
    private final long id;
    private final LocalDateTime createdAt;
    private final BigDecimal amount;
}