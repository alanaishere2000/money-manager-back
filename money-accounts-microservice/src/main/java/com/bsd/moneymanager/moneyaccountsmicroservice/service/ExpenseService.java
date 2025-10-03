package com.bsd.moneymanager.moneyaccountsmicroservice.service;

import com.bsd.moneymanager.moneyaccountsmicroservice.dto.ExpenseBasis;
import com.bsd.moneymanager.moneyaccountsmicroservice.dto.ExpenseDetails;
import com.bsd.moneymanager.moneyaccountsmicroservice.dto.TransactionDetails;
import com.bsd.moneymanager.moneyaccountsmicroservice.entity.Expense;
import com.bsd.moneymanager.moneyaccountsmicroservice.entity.MoneyAccount;
import com.bsd.moneymanager.moneyaccountsmicroservice.mapper.ExpenseMapper;
import com.bsd.moneymanager.moneyaccountsmicroservice.repository.ExpenseRepository;
import com.bsd.moneymanager.moneyaccountsmicroservice.repository.MoneyAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.bsd.moneymanager.moneyaccountsmicroservice.utils.Constants.SORT_DESC_BY_CREATED_AT;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private MoneyAccountRepository moneyAccountRepository;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ExpenseMapper expenseMapper;

    public void createExpense(ExpenseBasis expenseBasis) {
        Expense expense = expenseMapper.basisToEntity(expenseBasis);

        Optional<MoneyAccount> optionalMoneyAccount = moneyAccountRepository
                .findById(expenseBasis.getMoneyAccountId());
        optionalMoneyAccount.ifPresent(expense::setMoneyAccount);

        expenseRepository.save(expense);
    }

    public List<ExpenseDetails> getAllExpensesOfMoneyAccount(long moneyAccountId) {
        List<Expense> allExpensesOfMoneyAccount = expenseRepository
                .findByMoneyAccountId(moneyAccountId, SORT_DESC_BY_CREATED_AT);

        return expenseMapper.entityListToDetailsList(allExpensesOfMoneyAccount);
    }

    public List<ExpenseDetails> getAllExpensesOfMoneyAccountByCategory(long moneyAccountId, String category) {
        List<Expense> allExpensesOfMoneyAccount = expenseRepository
                .findByMoneyAccountIdAndCategory(moneyAccountId, category, SORT_DESC_BY_CREATED_AT);

        return expenseMapper.entityListToDetailsList(allExpensesOfMoneyAccount);
    }

    public List<TransactionDetails> getAllExpensesOfUser(String username) {
        return transactionService.getAllTransactionsOfUser(username, this::createMapForExpensesOfMoneyAccount);
    }

    public List<TransactionDetails> getAllExpensesOfUser(String username, String category) {
        return transactionService.filterByExpenseCategory(getAllExpensesOfUser(username), category);
    }

    public List<TransactionDetails> getMonthlyExpensesOfUser(String username, int month) {
        return transactionService.filterByMonth(getAllExpensesOfUser(username), month);
    }

    public List<TransactionDetails> getMonthlyExpensesOfUser(String username, int month, String category) {
        return transactionService.filterByExpenseCategory(getMonthlyExpensesOfUser(username, month), category);
    }

    public List<TransactionDetails> getCurrentMonthExpensesOfUser(String username) {
        int currentMonth = LocalDate.now().getMonthValue();
        return getMonthlyExpensesOfUser(username, currentMonth);
    }

    public List<TransactionDetails> getCurrentMonthExpensesOfUser(String username, String category) {
        return transactionService.filterByExpenseCategory(getCurrentMonthExpensesOfUser(username), category);
    }

    public BigDecimal getAmountOfAllExpensesOfUser(String username) {
        return transactionService.computeAmountSum(getAllExpensesOfUser(username));
    }

    public BigDecimal getAmountOfAllExpensesOfUser(String username, String category) {
        return transactionService.computeAmountSum(getAllExpensesOfUser(username, category));
    }

    public BigDecimal getAmountOfMonthlyExpensesOfUser(String username, int month) {
        return transactionService.computeAmountSum(getMonthlyExpensesOfUser(username, month));
    }

    public BigDecimal getAmountOfMonthlyExpensesOfUser(String username, int month, String category) {
        return transactionService.computeAmountSum(getMonthlyExpensesOfUser(username, month, category));
    }

    public BigDecimal getAmountOfCurrentMonthExpensesOfUser(String username) {
        return transactionService.computeAmountSum(getCurrentMonthExpensesOfUser(username));
    }

    public BigDecimal getAmountOfCurrentMonthExpensesOfUser(String username, String category) {
        return transactionService.computeAmountSum(getCurrentMonthExpensesOfUser(username, category));
    }

    private Map<MoneyAccount, List<TransactionDetails>> createMapForExpensesOfMoneyAccount(List<MoneyAccount> moneyAccounts) {
        return moneyAccounts.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        moneyAccount -> convertExpenseDetailsToTransactionDetails(
                                getAllExpensesOfMoneyAccount(moneyAccount.getId())
                        )
                ));
    }

    private List<TransactionDetails> convertExpenseDetailsToTransactionDetails(List<ExpenseDetails> expenseDetails) {
        return expenseMapper.expenseDetailsToTransactionDetails(expenseDetails);
    }
}