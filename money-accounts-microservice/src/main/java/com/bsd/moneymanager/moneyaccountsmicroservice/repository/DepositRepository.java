package com.bsd.moneymanager.moneyaccountsmicroservice.repository;

import com.bsd.moneymanager.moneyaccountsmicroservice.entity.Deposit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, Long> {
    List<Deposit> findByMoneyAccountId(long moneyAccountId, Sort sort);
}