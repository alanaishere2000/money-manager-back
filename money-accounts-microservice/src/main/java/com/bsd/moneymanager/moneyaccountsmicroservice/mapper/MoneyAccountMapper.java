package com.bsd.moneymanager.moneyaccountsmicroservice.mapper;

import com.bsd.moneymanager.moneyaccountsmicroservice.dto.MoneyAccountBasis;
import com.bsd.moneymanager.moneyaccountsmicroservice.dto.MoneyAccountDetails;
import com.bsd.moneymanager.moneyaccountsmicroservice.dto.MoneyAccountPreview;
import com.bsd.moneymanager.moneyaccountsmicroservice.entity.MoneyAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class},
        uses = DepositMapper.class)
public abstract class MoneyAccountMapper {

    public abstract MoneyAccountPreview entityToPreview(MoneyAccount moneyAccount);

    public abstract List<MoneyAccountPreview> entityListToPreviewList(List<MoneyAccount> moneyAccount);

    public abstract MoneyAccountDetails entityToDetails(MoneyAccount moneyAccount);

    @Mapping(expression = "java(LocalDateTime.now())", target = "createdAt")
    public abstract MoneyAccount basisToEntity(MoneyAccountBasis moneyAccountBasis);
}