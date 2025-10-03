package com.bsd.moneymanager.moneyaccountsmicroservice.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode
@Getter
@Setter
public class MoneyAccountPreview {
    private long id;
    private String name;
    private String currency;
    private LocalDateTime createdAt;
    private BigDecimal amount;
}