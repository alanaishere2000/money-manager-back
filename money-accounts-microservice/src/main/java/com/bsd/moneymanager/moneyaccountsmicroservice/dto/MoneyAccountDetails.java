package com.bsd.moneymanager.moneyaccountsmicroservice.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode
@Getter
@Setter
public class MoneyAccountDetails {
    private long id;
    private String username;
    private LocalDateTime createdAt;
    private String currency;
    private String name;
    private String description;
    private List<TransactionDetails> transactions;
}