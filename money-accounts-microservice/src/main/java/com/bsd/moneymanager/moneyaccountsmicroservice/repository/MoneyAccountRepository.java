package com.bsd.moneymanager.moneyaccountsmicroservice.repository;

import com.bsd.moneymanager.moneyaccountsmicroservice.entity.MoneyAccount;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.PageFormat;
import java.util.List;

@Repository
public interface MoneyAccountRepository extends JpaRepository<MoneyAccount, Long> {
    List<MoneyAccount> findByUsername(String username, Sort sort);
    List<MoneyAccount> findByUsername(String username, PageRequest pageRequest);
}