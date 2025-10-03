package com.bsd.moneymanager.moneyaccountsmicroservice.controller;

import com.bsd.moneymanager.moneyaccountsmicroservice.dto.ExpenseBasis;
import com.bsd.moneymanager.moneyaccountsmicroservice.dto.ExpenseDetails;
import com.bsd.moneymanager.moneyaccountsmicroservice.dto.TransactionDetails;
import com.bsd.moneymanager.moneyaccountsmicroservice.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/expense")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @CrossOrigin
    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void createExpense(@RequestBody ExpenseBasis expenseBasis) {
        expenseService.createExpense(expenseBasis);
    }

    @CrossOrigin
    @GetMapping(produces = "application/json")
    public List<ExpenseDetails> getAllExpensesOfMoneyAccount(@RequestParam long moneyAccountId,
                                                             @RequestParam Optional<String> category) {
        if (category.isPresent()) {
            return expenseService.getAllExpensesOfMoneyAccountByCategory(moneyAccountId, category.get());
        }

        return expenseService.getAllExpensesOfMoneyAccount(moneyAccountId);
    }

    @CrossOrigin
    @GetMapping(value = "history", produces = "application/json")
    public List<TransactionDetails> getAllExpensesOfUser(@RequestParam String username,
                                                         @RequestParam Optional<String> category) {
        if (category.isPresent()) {
            return expenseService.getAllExpensesOfUser(username, category.get());
        }

        return expenseService.getAllExpensesOfUser(username);
    }

    @CrossOrigin
    @GetMapping(value = "history/amount", produces = "application/json")
    public BigDecimal getAmountOfAllExpensesOfUser(@RequestParam String username,
                                                   @RequestParam Optional<String> category) {
        if (category.isPresent()) {
            return expenseService.getAmountOfAllExpensesOfUser(username, category.get());
        }

        return expenseService.getAmountOfAllExpensesOfUser(username);
    }

    @CrossOrigin
    @GetMapping(value = "monthly", produces = "application/json")
    public List<TransactionDetails> getMonthlyExpensesOfUser(@RequestParam String username,
                                                             @RequestParam Optional<Integer> month,
                                                             @RequestParam Optional<String> category) {
        if (month.isPresent() && category.isPresent()) {
            return expenseService.getMonthlyExpensesOfUser(username, month.get(), category.get());
        }

        if (month.isPresent()) {
            return expenseService.getMonthlyExpensesOfUser(username, month.get());
        }

        if (category.isPresent()) {
            return expenseService.getCurrentMonthExpensesOfUser(username, category.get());
        }

        return expenseService.getCurrentMonthExpensesOfUser(username);
    }

    @CrossOrigin
    @GetMapping(value = "monthly/amount", produces = "application/json")
    public BigDecimal getAmountOfMonthlyExpensesOfUser(@RequestParam String username,
                                                       @RequestParam Optional<Integer> month,
                                                       @RequestParam Optional<String> category) {
        if (month.isPresent() && category.isPresent()) {
            return expenseService.getAmountOfMonthlyExpensesOfUser(username, month.get(), category.get());
        }

        if (month.isPresent()) {
            return expenseService.getAmountOfMonthlyExpensesOfUser(username, month.get());
        }

        if (category.isPresent()) {
            return expenseService.getAmountOfCurrentMonthExpensesOfUser(username, category.get());
        }

        return expenseService.getAmountOfCurrentMonthExpensesOfUser(username);
    }
}