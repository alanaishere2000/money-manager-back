package com.bsd.moneymanager.moneyaccountsmicroservice.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor  // Good practice because some libraries need one
public enum TransactionType {
    DEPOSIT("deposit"),
    EXPENSE("expense");

    private String name;
}