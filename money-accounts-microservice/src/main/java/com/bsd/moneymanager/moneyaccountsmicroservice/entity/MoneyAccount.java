package com.bsd.moneymanager.moneyaccountsmicroservice.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
public class MoneyAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private LocalDateTime createdAt;

    @Column(length=3, nullable=false)
    private String currency;

    @Column(length=50, nullable=false)
    private String name;

    @Column(length=250)
    private String description;

    @OneToMany(mappedBy = "moneyAccount", fetch = FetchType.LAZY)
    private Set<Deposit> deposits;

    @OneToMany(mappedBy = "moneyAccount", fetch = FetchType.LAZY)
    private Set<Expense> expenses;
}