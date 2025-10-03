package com.bsd.moneymanager.moneyaccountsmicroservice.utils;

import org.springframework.data.domain.Sort;

public class Constants {
    public static final Sort SORT_DESC_BY_CREATED_AT = Sort.by(Sort.Direction.DESC, "createdAt");
}