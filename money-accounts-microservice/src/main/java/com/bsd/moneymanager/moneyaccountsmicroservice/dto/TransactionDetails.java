package com.bsd.moneymanager.moneyaccountsmicroservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionDetails {
    private long id;
    private LocalDateTime createdAt;
    private BigDecimal amount;
    private String type;
    private String expenseCategory;
    private String moneyAccountName;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long moneyAccountId;
}