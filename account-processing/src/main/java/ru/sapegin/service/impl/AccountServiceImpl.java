package ru.sapegin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sapegin.dto.AccountDTO;
import ru.sapegin.dto.ClientProductDTO;
import ru.sapegin.dto.TransactionDTO;
import ru.sapegin.model.Account;
import ru.sapegin.repository.AccountRepository;
import ru.sapegin.service.AccountServiceI;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountServiceI {

    private final AccountRepository accountRepository;
    private final PaymentServiceImpl paymentServiceImpl;

    @Transactional
    @Override
    public AccountDTO create(ClientProductDTO accountDTO) {
        var account = new Account(
                accountDTO.clientId(),
                accountDTO.productId(),
                BigDecimal.ZERO,
                BigDecimal.valueOf(0.01),
                "CC".equals(accountDTO.key()) || "NS".equals(accountDTO.key()),
                false,
                "ACTIVE"
        );
        accountRepository.save(account);
        if(account.isRecalc()){
            paymentServiceImpl.createCreditPayments(account, 5);
        }
        log.info("СОЗДАН Account: {}", account);
        return mapToDTO(account);
    }

    public Account proccessAccount(TransactionDTO transactionDTO){
        var account = getAccountById(transactionDTO.getAccountId());
        if(account.getStatus().equals("ARRESTED") || account.getStatus().equals("BLOCKED")){
            throw new RuntimeException("Аккаунт заблокирован");
        }
        if(transactionDTO.getType().equals("DEBITING")){
            account.setBalance(account.getBalance().subtract(transactionDTO.getAmount()));
        } else if(transactionDTO.getType().equals("ACCRUAL")){
            account.setBalance(account.getBalance().add(transactionDTO.getAmount()));
        }
        return account;
    }

    @Override
    public AccountDTO mapToDTO(Account account) {
        return new AccountDTO(
                account.getClientId(),
                account.getProductId(),
                account.getBalance(),
                account.getInterestRate(),
                account.isRecalc(),
                account.isCardExist(),
                account.getStatus()
        );
    }

    @Override
    public Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account с таким id не найден"));
    }
}
