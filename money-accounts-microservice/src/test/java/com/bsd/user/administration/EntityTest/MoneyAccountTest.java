package com.bsd.user.administration.EntityTest;

import com.bsd.moneymanager.moneyaccountsmicroservice.entity.Deposit;
import com.bsd.moneymanager.moneyaccountsmicroservice.entity.Expense;
import com.bsd.moneymanager.moneyaccountsmicroservice.entity.MoneyAccount;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class MoneyAccountTest {

    @Test
    void testMoneyAccountFields() {
        LocalDateTime now = LocalDateTime.now();

        MoneyAccount account = new MoneyAccount();
        account.setId(1L);
        account.setUsername("john");
        account.setCreatedAt(now);
        account.setCurrency("USD");
        account.setName("Main Account");
        account.setDescription("Personal savings");

        assertThat(account.getId()).isEqualTo(1L);
        assertThat(account.getUsername()).isEqualTo("john");
        assertThat(account.getCreatedAt()).isEqualTo(now);
        assertThat(account.getCurrency()).isEqualTo("USD");
        assertThat(account.getName()).isEqualTo("Main Account");
        assertThat(account.getDescription()).isEqualTo("Personal savings");
    }

    @Test
    void testMoneyAccountRelations() {
        MoneyAccount account = new MoneyAccount();

        Deposit deposit = new Deposit();
        deposit.setId(10L);
        deposit.setMoneyAccount(account);

        Expense expense = new Expense();
        expense.setId(20L);
        expense.setMoneyAccount(account);

        account.setDeposits(Set.of(deposit));
        account.setExpenses(Set.of(expense));

        assertThat(account.getDeposits()).hasSize(1);
        assertThat(account.getDeposits().iterator().next().getId()).isEqualTo(10L);

        assertThat(account.getExpenses()).hasSize(1);
        assertThat(account.getExpenses().iterator().next().getId()).isEqualTo(20L);
    }
}
