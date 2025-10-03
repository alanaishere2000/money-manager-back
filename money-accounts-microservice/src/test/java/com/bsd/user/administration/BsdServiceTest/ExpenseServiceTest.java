package com.bsd.user.administration.BsdServiceTest;

import com.bsd.moneymanager.moneyaccountsmicroservice.dto.ExpenseBasis;
import com.bsd.moneymanager.moneyaccountsmicroservice.dto.ExpenseDetails;
import com.bsd.moneymanager.moneyaccountsmicroservice.dto.TransactionDetails;
import com.bsd.moneymanager.moneyaccountsmicroservice.entity.Expense;
import com.bsd.moneymanager.moneyaccountsmicroservice.entity.MoneyAccount;
import com.bsd.moneymanager.moneyaccountsmicroservice.mapper.ExpenseMapper;
import com.bsd.moneymanager.moneyaccountsmicroservice.repository.ExpenseRepository;
import com.bsd.moneymanager.moneyaccountsmicroservice.repository.MoneyAccountRepository;
import com.bsd.moneymanager.moneyaccountsmicroservice.service.ExpenseService;
import com.bsd.moneymanager.moneyaccountsmicroservice.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ExpenseServiceTest {

    @InjectMocks
    private ExpenseService expenseService;

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private MoneyAccountRepository moneyAccountRepository;

    @Mock
    private TransactionService transactionService;

    @Mock
    private ExpenseMapper expenseMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllExpensesOfUser_ShouldDelegateToTransactionService() {
        TransactionDetails transactionDetails = new TransactionDetails();

        when(transactionService.getAllTransactionsOfUser(eq("john"), any()))
                .thenReturn(List.of(transactionDetails));

        List<TransactionDetails> result = expenseService.getAllExpensesOfUser("john");

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(transactionDetails);
    }

    @Test
    void testGetAmountOfAllExpensesOfUser_ShouldReturnSum() {
        when(transactionService.getAllTransactionsOfUser(eq("john"), any()))
                .thenReturn(Collections.emptyList());
        when(transactionService.computeAmountSum(anyList())).thenReturn(BigDecimal.TEN);

        BigDecimal result = expenseService.getAmountOfAllExpensesOfUser("john");

        assertThat(result).isEqualTo(BigDecimal.TEN);
    }
}
