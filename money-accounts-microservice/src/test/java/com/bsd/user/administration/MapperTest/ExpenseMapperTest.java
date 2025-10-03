package com.bsd.user.administration.MapperTest;

import com.bsd.moneymanager.moneyaccountsmicroservice.dto.ExpenseBasis;
import com.bsd.moneymanager.moneyaccountsmicroservice.dto.ExpenseDetails;
import com.bsd.moneymanager.moneyaccountsmicroservice.dto.TransactionDetails;
import com.bsd.moneymanager.moneyaccountsmicroservice.entity.Expense;
import com.bsd.moneymanager.moneyaccountsmicroservice.mapper.ExpenseMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ExpenseMapperTest {

    private final ExpenseMapper mapper = Mappers.getMapper(ExpenseMapper.class);

    @Test
    void testEntityToDetails() {
        Expense expense = new Expense();
        expense.setId(1L);
        expense.setCategory("food");
        expense.setAmount(BigDecimal.valueOf(25.50));
        expense.setCreatedAt(LocalDateTime.now());

        ExpenseDetails details = mapper.entityToDetails(expense);

        assertThat(details).isNotNull();
        assertThat(details.getCategory()).isEqualTo("food");
        assertThat(details.getAmount()).isEqualByComparingTo("25.50");
    }

    @Test
    void testEntityListToDetailsList() {
        Expense expense = new Expense();
        expense.setCategory("transport");
        expense.setAmount(BigDecimal.TEN);

        List<ExpenseDetails> detailsList = mapper.entityListToDetailsList(List.of(expense));

        assertThat(detailsList).hasSize(1);
        assertThat(detailsList.get(0).getCategory()).isEqualTo("transport");
    }


    @Test
    void testEntitySetToDetailsSet() {
        Expense expense = new Expense();
        expense.setCategory("fun");
        expense.setAmount(BigDecimal.ONE);

        Set<ExpenseDetails> detailsSet = mapper.entitySetToDetailsSet(Set.of(expense));

        assertThat(detailsSet).hasSize(1);
        assertThat(detailsSet.iterator().next().getCategory()).isEqualTo("fun");
    }
}
