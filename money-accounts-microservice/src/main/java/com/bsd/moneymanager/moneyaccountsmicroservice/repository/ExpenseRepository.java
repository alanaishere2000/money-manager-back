package com.bsd.moneymanager.moneyaccountsmicroservice.repository;

import com.bsd.moneymanager.moneyaccountsmicroservice.entity.Expense;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByMoneyAccountId(long moneyAccountId, Sort sort);
    List<Expense> findByMoneyAccountIdAndCategory(long moneyAccountId, String category, Sort sort);
}