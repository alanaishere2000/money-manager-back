package com.bsd.moneymanager.moneyaccountsmicroservice.service;

import com.bsd.moneymanager.moneyaccountsmicroservice.dto.DepositDetails;
import com.bsd.moneymanager.moneyaccountsmicroservice.dto.ExpenseDetails;
import com.bsd.moneymanager.moneyaccountsmicroservice.dto.MoneyAccountBasis;
import com.bsd.moneymanager.moneyaccountsmicroservice.dto.MoneyAccountDetails;
import com.bsd.moneymanager.moneyaccountsmicroservice.dto.MoneyAccountPreview;
import com.bsd.moneymanager.moneyaccountsmicroservice.dto.TransactionDetails;
import com.bsd.moneymanager.moneyaccountsmicroservice.entity.Deposit;
import com.bsd.moneymanager.moneyaccountsmicroservice.entity.Expense;
import com.bsd.moneymanager.moneyaccountsmicroservice.entity.MoneyAccount;
import com.bsd.moneymanager.moneyaccountsmicroservice.mapper.DepositMapper;
import com.bsd.moneymanager.moneyaccountsmicroservice.mapper.ExpenseMapper;
import com.bsd.moneymanager.moneyaccountsmicroservice.mapper.MoneyAccountMapper;
import com.bsd.moneymanager.moneyaccountsmicroservice.repository.MoneyAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.bsd.moneymanager.moneyaccountsmicroservice.utils.Constants.SORT_DESC_BY_CREATED_AT;

@Service
public class MoneyAccountService {

    @Autowired
    private MoneyAccountRepository moneyAccountRepository;

    @Autowired
    private MoneyAccountMapper moneyAccountMapper;
    @Autowired
    private ExpenseMapper expenseMapper;
    @Autowired
    private DepositMapper depositMapper;

    public void createMoneyAccount(MoneyAccountBasis moneyAccountBasis) {
        MoneyAccount moneyAccount = moneyAccountMapper.basisToEntity(moneyAccountBasis);
        moneyAccountRepository.save(moneyAccount);
    }

    public List<MoneyAccountPreview> getRecentMoneyAccountPreviews(String username) {
        int limitRecent = 3;

        List<MoneyAccount> recentMoneyAccounts = moneyAccountRepository
                .findByUsername(username, PageRequest.of(0, limitRecent, SORT_DESC_BY_CREATED_AT));

        return getMoneyAccountPreviewsWithAmount(recentMoneyAccounts);
    }

    public List<MoneyAccountPreview> getAllMoneyAccountPreviews(String username) {
        List<MoneyAccount> allMoneyAccounts = moneyAccountRepository.findByUsername(username, SORT_DESC_BY_CREATED_AT);
        return getMoneyAccountPreviewsWithAmount(allMoneyAccounts);
    }

    private List<MoneyAccountPreview> getMoneyAccountPreviewsWithAmount(List<MoneyAccount> moneyAccounts) {
        List<MoneyAccountPreview> moneyAccountPreviews = moneyAccountMapper.entityListToPreviewList(moneyAccounts);

        Map<Long, MoneyAccount> moneyAccountIndexMap = moneyAccounts.stream()
                .collect(Collectors.toMap(MoneyAccount::getId, Function.identity()));

        moneyAccountPreviews.forEach(moneyAccountPreview -> {
            MoneyAccount moneyAccount = moneyAccountIndexMap.get(moneyAccountPreview.getId());
            BigDecimal currentAmount = computeCurrentAmountInMoneyAccount(moneyAccount);
            moneyAccountPreview.setAmount(currentAmount);
        });

        return moneyAccountPreviews;
    }

    private BigDecimal computeCurrentAmountInMoneyAccount(MoneyAccount moneyAccount) {
        BigDecimal sumDeposits = moneyAccount.getDeposits().stream()
                .map(Deposit::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal sumExpenses = moneyAccount.getExpenses().stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return sumDeposits.subtract(sumExpenses);
    }

    public MoneyAccountDetails getMoneyAccountDetails(long id) {
        Optional<MoneyAccount> optionalMoneyAccount = moneyAccountRepository.findById(id);

        if (optionalMoneyAccount.isEmpty()) {
            return new MoneyAccountDetails();
        }

        MoneyAccount moneyAccount = optionalMoneyAccount.get();
        MoneyAccountDetails moneyAccountDetails = moneyAccountMapper.entityToDetails(moneyAccount);
        moneyAccountDetails.setTransactions(getTransactionDetails(moneyAccount));

        return moneyAccountDetails;
    }

    public MoneyAccountDetails getMoneyAccountDetails(long id,
                                                      String transactionType) {
        Optional<MoneyAccount> optionalMoneyAccount = moneyAccountRepository.findById(id);

        if (optionalMoneyAccount.isEmpty()) {
            return new MoneyAccountDetails();
        }

        MoneyAccount moneyAccount = optionalMoneyAccount.get();
        MoneyAccountDetails moneyAccountDetails = moneyAccountMapper.entityToDetails(moneyAccount);
        moneyAccountDetails.setTransactions(getTransactionDetails(moneyAccount, transactionType));

        return moneyAccountDetails;
    }

    public MoneyAccountDetails getMoneyAccountDetails(long id,
                                                      String transactionType,  // checked it's "expense" in controller
                                                      String expenseCategory) {
        Optional<MoneyAccount> optionalMoneyAccount = moneyAccountRepository.findById(id);

        if (optionalMoneyAccount.isEmpty()) {
            return new MoneyAccountDetails();
        }

        MoneyAccount moneyAccount = optionalMoneyAccount.get();
        MoneyAccountDetails moneyAccountDetails = moneyAccountMapper.entityToDetails(moneyAccount);
        moneyAccountDetails.setTransactions(getTransactionDetailsByExpenseCategory(moneyAccount, expenseCategory));

        return moneyAccountDetails;
    }

    private List<TransactionDetails> getTransactionDetails(MoneyAccount moneyAccount) {
        List<DepositDetails> deposits = depositMapper.entitySetToDetailsList(moneyAccount.getDeposits());
        List<ExpenseDetails> expenses = expenseMapper.entitySetToDetailsList(moneyAccount.getExpenses());

        List<TransactionDetails> transactions = new ArrayList<>();
        transactions.addAll(depositMapper.depositDetailsListToTransactionDetailsList(deposits));
        transactions.addAll(expenseMapper.expenseDetailsToTransactionDetails(expenses));

        sortTransactionsByCreationDateDescending(transactions);

        return transactions;
    }

    private List<TransactionDetails> getTransactionDetails(MoneyAccount moneyAccount,
                                                           String transactionType) {
        if (transactionType.equals("deposit")) {
            return getTransactionDetailsByDeposit(moneyAccount);
        }

        if (transactionType.equals("expense")) {
            return getTransactionDetailsByExpense(moneyAccount);
        }

        return new ArrayList<>();
    }

    private List<TransactionDetails> getTransactionDetailsByDeposit(MoneyAccount moneyAccount) {
        List<DepositDetails> deposits = depositMapper.entitySetToDetailsList(moneyAccount.getDeposits());

        List<TransactionDetails> transactions = depositMapper.depositDetailsListToTransactionDetailsList(deposits);
        sortTransactionsByCreationDateDescending(transactions);

        return transactions;
    }

    private List<TransactionDetails> getTransactionDetailsByExpense(MoneyAccount moneyAccount) {
        List<ExpenseDetails> expenses = expenseMapper.entitySetToDetailsList(moneyAccount.getExpenses());

        List<TransactionDetails> transactions = expenseMapper.expenseDetailsToTransactionDetails(expenses);
        sortTransactionsByCreationDateDescending(transactions);

        return transactions;
    }

    private List<TransactionDetails> getTransactionDetailsByExpenseCategory(MoneyAccount moneyAccount,
                                                                            String expenseCategory) {
        List<ExpenseDetails> expenses = expenseMapper.entitySetToDetailsList(moneyAccount.getExpenses());
        List<ExpenseDetails> expensesByCategory = expenses.stream()
                .filter(expense -> expense.getCategory().equals(expenseCategory))
                .collect(Collectors.toList());

        List<TransactionDetails> transactions = expenseMapper.expenseDetailsToTransactionDetails(expensesByCategory);
        sortTransactionsByCreationDateDescending(transactions);

        return transactions;
    }

    private void sortTransactionsByCreationDateDescending(List<TransactionDetails> transactions) {
        transactions.sort(Comparator.comparing(TransactionDetails::getCreatedAt).reversed());
    }
}