package com.bsd.moneymanager.moneyaccountsmicroservice.service;

import com.bsd.moneymanager.moneyaccountsmicroservice.dto.TransactionDetails;
import com.bsd.moneymanager.moneyaccountsmicroservice.entity.MoneyAccount;
import com.bsd.moneymanager.moneyaccountsmicroservice.repository.MoneyAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.bsd.moneymanager.moneyaccountsmicroservice.utils.Constants.SORT_DESC_BY_CREATED_AT;

@Service
public class TransactionService {

    @Autowired
    private MoneyAccountRepository moneyAccountRepository;

    public List<TransactionDetails> getAllTransactionsOfUser(String username,
                                                             Function<List<MoneyAccount>,
                                                                     Map<MoneyAccount,
                                                                             List<TransactionDetails>>> creatorOfMap) {
        List<MoneyAccount> moneyAccountsOfUser = moneyAccountRepository
                .findByUsername(username, SORT_DESC_BY_CREATED_AT);

        Map<MoneyAccount, List<TransactionDetails>> expensesOfMoneyAccountMap =
                creatorOfMap.apply(moneyAccountsOfUser);

        return expensesOfMoneyAccountMap.entrySet().stream()
                .map(entry -> addMoneyAccountToTransactionDetails(entry.getKey(), entry.getValue()))
                .flatMap(List::stream)
                .sorted(Comparator.comparing(TransactionDetails::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    public BigDecimal computeAmountSum(List<TransactionDetails> transactions) {
        return transactions.stream()
                .map(TransactionDetails::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<TransactionDetails> filterByMonth(List<TransactionDetails> transactions,
                                                  int month) {
        return filterTransactions(transactions,
                transactionDetails -> month == transactionDetails.getCreatedAt().getMonthValue());
    }

    public List<TransactionDetails> filterByExpenseCategory(List<TransactionDetails> transactions,
                                                            String expenseCategory) {
        return filterTransactions(transactions,
                transactionDetails -> expenseCategory.equals(transactionDetails.getExpenseCategory()));
    }

    private List<TransactionDetails> filterTransactions(List<TransactionDetails> transactions,
                                                        Predicate<TransactionDetails> predicate) {
        return transactions.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    private List<TransactionDetails> addMoneyAccountToTransactionDetails(MoneyAccount moneyAccount,
                                                                         List<TransactionDetails> transactionDetailsList) {
        return transactionDetailsList.stream()
                .peek(transactionDetails -> {
                    transactionDetails.setMoneyAccountId(moneyAccount.getId());
                    transactionDetails.setMoneyAccountName(moneyAccount.getName());
                })
                .collect(Collectors.toList());
    }
}