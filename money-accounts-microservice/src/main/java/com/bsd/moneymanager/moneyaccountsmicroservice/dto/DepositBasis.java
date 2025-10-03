package com.bsd.moneymanager.moneyaccountsmicroservice.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@EqualsAndHashCode
@Getter
public class DepositBasis {
    public final long moneyAccountId;
    public final BigDecimal amount;
}