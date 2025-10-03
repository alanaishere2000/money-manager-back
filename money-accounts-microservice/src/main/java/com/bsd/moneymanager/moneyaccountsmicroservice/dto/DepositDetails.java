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
    public final long id;
    public final LocalDateTime createdAt;
    public final BigDecimal amount;
}