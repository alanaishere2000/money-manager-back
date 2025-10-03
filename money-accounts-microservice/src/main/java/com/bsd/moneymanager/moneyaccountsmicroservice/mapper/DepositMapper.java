package com.bsd.moneymanager.moneyaccountsmicroservice.mapper;

import com.bsd.moneymanager.moneyaccountsmicroservice.dto.DepositBasis;
import com.bsd.moneymanager.moneyaccountsmicroservice.dto.DepositDetails;
import com.bsd.moneymanager.moneyaccountsmicroservice.dto.TransactionDetails;
import com.bsd.moneymanager.moneyaccountsmicroservice.entity.Deposit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class})
public abstract class DepositMapper {

    public abstract DepositDetails entityToDetails(Deposit deposit);

    public abstract List<DepositDetails> entityListToDetailsList(List<Deposit> deposit);

    public abstract List<DepositDetails> entitySetToDetailsList(Set<Deposit> deposit);

    public abstract Set<DepositDetails> entitySetToDetailsSet(Set<Deposit> deposit);

    @Mapping(target = "type", constant = "deposit")
    public abstract TransactionDetails depositDetailsToTransactionDetails(DepositDetails depositDetails);

    public abstract List<TransactionDetails> depositDetailsListToTransactionDetailsList(List<DepositDetails> depositDetails);

    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    public abstract Deposit basisToEntity(DepositBasis depositBasis);
}