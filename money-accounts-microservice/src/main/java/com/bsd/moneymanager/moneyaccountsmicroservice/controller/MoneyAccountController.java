package com.bsd.moneymanager.moneyaccountsmicroservice.controller;

import com.bsd.moneymanager.moneyaccountsmicroservice.dto.MoneyAccountBasis;
import com.bsd.moneymanager.moneyaccountsmicroservice.dto.MoneyAccountDetails;
import com.bsd.moneymanager.moneyaccountsmicroservice.dto.MoneyAccountPreview;
import com.bsd.moneymanager.moneyaccountsmicroservice.service.MoneyAccountService;
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

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/money-account")
public class MoneyAccountController {

    @Autowired
    private MoneyAccountService moneyAccountService;

    @CrossOrigin
    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void createMoneyAccount(@RequestBody MoneyAccountBasis moneyAccountBasis) {
        moneyAccountService.createMoneyAccount(moneyAccountBasis);
    }

    @CrossOrigin
    @GetMapping(value = "/preview/recent", produces = "application/json")
    public List<MoneyAccountPreview> getRecentMoneyAccountPreviews(@RequestParam String username) {
        return moneyAccountService.getRecentMoneyAccountPreviews(username);
    }

    @CrossOrigin
    @GetMapping(value = "/preview", produces = "application/json")
    public List<MoneyAccountPreview> getAllMoneyAccountPreviews(@RequestParam String username) {
        return moneyAccountService.getAllMoneyAccountPreviews(username);
    }

    @CrossOrigin
    @GetMapping(produces = "application/json")
    public MoneyAccountDetails getMoneyAccountDetails(@RequestParam long id,
                                                      @RequestParam Optional<String> transactionType,
                                                      @RequestParam Optional<String> expenseCategory) {
        if (transactionType.isPresent() && expenseCategory.isPresent()) {
            if (!transactionType.get().equalsIgnoreCase("expense")) {
                return new MoneyAccountDetails();  // Maybe some response with error
            }

            return moneyAccountService.getMoneyAccountDetails(id, transactionType.get(), expenseCategory.get());
        }

        if (transactionType.isPresent()) {
            return moneyAccountService.getMoneyAccountDetails(id, transactionType.get());
        }

        return moneyAccountService.getMoneyAccountDetails(id);
    }
}