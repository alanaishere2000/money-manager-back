package com.bsd.user.administration.BsdServiceTest;
import com.bsd.moneymanager.moneyaccountsmicroservice.entity.MoneyAccount;
import com.bsd.moneymanager.moneyaccountsmicroservice.repository.MoneyAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MoneyAccountService {

    private final MoneyAccountRepository moneyAccountRepository;

    public MoneyAccount createAccount(MoneyAccount moneyAccount) {
        log.info("Creating MoneyAccount for user={}", moneyAccount.getUsername());

        if (moneyAccount.getCurrency() == null || moneyAccount.getCurrency().isBlank()) {
            log.error("Currency missing for account creation, user={}", moneyAccount.getUsername());
            throw new IllegalArgumentException("Currency must not be empty");
        }

        return moneyAccountRepository.save(moneyAccount);
    }

    public Optional<MoneyAccount> getAccountById(long id) {
        log.debug("Fetching MoneyAccount id={}", id);
        return moneyAccountRepository.findById(id);
    }
}
