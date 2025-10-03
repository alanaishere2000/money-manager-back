package com.bsd.moneymanager.moneyaccountsmicroservice.service;

import com.bsd.moneymanager.moneyaccountsmicroservice.dto.DepositBasis;
import com.bsd.moneymanager.moneyaccountsmicroservice.dto.DepositDetails;
import com.bsd.moneymanager.moneyaccountsmicroservice.dto.TransactionDetails;
import com.bsd.moneymanager.moneyaccountsmicroservice.entity.Deposit;
import com.bsd.moneymanager.moneyaccountsmicroservice.entity.MoneyAccount;
import com.bsd.moneymanager.moneyaccountsmicroservice.mapper.DepositMapper;
import com.bsd.moneymanager.moneyaccountsmicroservice.repository.DepositRepository;
import com.bsd.moneymanager.moneyaccountsmicroservice.repository.MoneyAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.bsd.moneymanager.moneyaccountsmicroservice.utils.Constants.SORT_DESC_BY_CREATED_AT;

@Service
public class DepositService {

    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private MoneyAccountRepository moneyAccountRepository;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private DepositMapper depositMapper;

    public void createDeposit(DepositBasis depositBasis) {
        Deposit deposit = depositMapper.basisToEntity(depositBasis);

        Optional<MoneyAccount> optionalMoneyAccount = moneyAccountRepository
                .findById(depositBasis.getMoneyAccountId());
        optionalMoneyAccount.ifPresent(deposit::setMoneyAccount);

        depositRepository.save(deposit);
    }

    public List<DepositDetails> getAllDepositsOfMoneyAccount(long moneyAccountId) {
        List<Deposit> allDepositsOfMoneyAccount = depositRepository
                .findByMoneyAccountId(moneyAccountId, SORT_DESC_BY_CREATED_AT);

        return depositMapper.entityListToDetailsList(allDepositsOfMoneyAccount);
    }

    public List<TransactionDetails> getAllDepositsOfUser(String username) {
        return transactionService.getAllTransactionsOfUser(username, this::createMapForDepositsOfMoneyAccount);
    }

    public List<TransactionDetails> getMonthlyDepositsOfUser(String username, int month) {
        return transactionService.filterByMonth(getAllDepositsOfUser(username), month);
    }

    public List<TransactionDetails> getCurrentMonthDepositsOfUser(String username) {
        int currentMonth = LocalDate.now().getMonthValue();
        return getMonthlyDepositsOfUser(username, currentMonth);
    }

    public BigDecimal getAmountOfAllDepositsOfUser(String username) {
        return transactionService.computeAmountSum(getAllDepositsOfUser(username));
    }

    public BigDecimal getAmountOfMonthlyDepositsOfUser(String username, int month) {
        return transactionService.computeAmountSum(getMonthlyDepositsOfUser(username, month));
    }

    public BigDecimal getAmountOfCurrentMonthDepositsOfUser(String username) {
        return transactionService.computeAmountSum(getCurrentMonthDepositsOfUser(username));
    }

    private Map<MoneyAccount, List<TransactionDetails>> createMapForDepositsOfMoneyAccount(List<MoneyAccount> moneyAccounts) {
        return moneyAccounts.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        moneyAccount -> convertDepositDetailsToTransactionDetails(
                                getAllDepositsOfMoneyAccount(moneyAccount.getId())
                        )
                ));
    }

    private List<TransactionDetails> convertDepositDetailsToTransactionDetails(List<DepositDetails> depositDetails) {
        return depositMapper.depositDetailsListToTransactionDetailsList(depositDetails);
    }
}