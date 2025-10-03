package com.bsd.moneymanager.moneyaccountsmicroservice.controller;

import com.bsd.moneymanager.moneyaccountsmicroservice.dto.DepositBasis;
import com.bsd.moneymanager.moneyaccountsmicroservice.dto.DepositDetails;
import com.bsd.moneymanager.moneyaccountsmicroservice.dto.TransactionDetails;
import com.bsd.moneymanager.moneyaccountsmicroservice.service.DepositService;
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
@RequestMapping(value = "/deposit")
public class DepositController {

    @Autowired
    private DepositService depositService;

    @CrossOrigin
    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void createDeposit(@RequestBody DepositBasis depositBasis) {
        depositService.createDeposit(depositBasis);
    }

    @CrossOrigin
    @GetMapping(produces = "application/json")
    public List<DepositDetails> getAllDepositsOfMoneyAccount(@RequestParam long moneyAccountId) {
        return depositService.getAllDepositsOfMoneyAccount(moneyAccountId);
    }

    @CrossOrigin
    @GetMapping(value = "history", produces = "application/json")
    public List<TransactionDetails> getAllDepositsOfUser(@RequestParam String username) {
        return depositService.getAllDepositsOfUser(username);
    }

    @CrossOrigin
    @GetMapping(value = "history/amount", produces = "application/json")
    public BigDecimal getAllAmountOfDepositsOfUser(@RequestParam String username) {
        return depositService.getAmountOfAllDepositsOfUser(username);
    }

    @CrossOrigin
    @GetMapping(value = "monthly", produces = "application/json")
    public List<TransactionDetails> getMonthlyDepositsOfUser(@RequestParam String username,
                                                             @RequestParam Optional<Integer> month) {
        if (month.isPresent()) {
            return depositService.getMonthlyDepositsOfUser(username, month.get());
        }

        return depositService.getCurrentMonthDepositsOfUser(username);
    }

    @CrossOrigin
    @GetMapping(value = "monthly/amount", produces = "application/json")
    public BigDecimal getAmountOfMonthlyDepositsOfUser(@RequestParam String username,
                                                       @RequestParam Optional<Integer> month) {
        if (month.isPresent()) {
            return depositService.getAmountOfMonthlyDepositsOfUser(username, month.get());
        }

        return depositService.getAmountOfCurrentMonthDepositsOfUser(username);
    }
}