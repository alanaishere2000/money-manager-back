package com.bsd.moneymanager.moneyaccountsmicroservice.mapper;

import com.bsd.moneymanager.moneyaccountsmicroservice.dto.ExpenseBasis;
import com.bsd.moneymanager.moneyaccountsmicroservice.dto.ExpenseDetails;
import com.bsd.moneymanager.moneyaccountsmicroservice.dto.TransactionDetails;
import com.bsd.moneymanager.moneyaccountsmicroservice.entity.Expense;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class})
public abstract class ExpenseMapper {

    public abstract ExpenseDetails entityToDetails(Expense expense);

    public abstract List<ExpenseDetails> entityListToDetailsList(List<Expense> expense);

    public abstract List<ExpenseDetails> entitySetToDetailsList(Set<Expense> expense);

    public abstract Set<ExpenseDetails> entitySetToDetailsSet(Set<Expense> expense);

    @Mapping(target = "type", constant = "expense")
    @Mapping(target = "expenseCategory", source = "category")
    public abstract TransactionDetails expenseDetailsToTransactionDetails(ExpenseDetails expenseDetails);

    public abstract List<TransactionDetails> expenseDetailsToTransactionDetails(List<ExpenseDetails> expenseDetails);

    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    public abstract Expense basisToEntity(ExpenseBasis expenseBasis);
}