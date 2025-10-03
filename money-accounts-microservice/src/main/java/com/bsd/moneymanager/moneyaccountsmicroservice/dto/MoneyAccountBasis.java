package com.bsd.moneymanager.moneyaccountsmicroservice.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@EqualsAndHashCode
@Getter
public class MoneyAccountBasis {
    private final String username;
    private final String name;
    private final String currency;
    private final String description;
}