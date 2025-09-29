package ru.sapegin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sapegin.dto.AccountDTO;
import ru.sapegin.dto.ClientProductDTO;
import ru.sapegin.dto.TransactionDTO;
import ru.sapegin.enums.AccountStatusEnum;
import ru.sapegin.enums.TransactionTypeEnum;
import ru.sapegin.model.Account;
import ru.sapegin.repository.AccountRepository;
import ru.sapegin.service.AccountServiceI;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountServiceI {

    private final AccountRepository accountRepository;

    @Transactional
    @Override
    public AccountDTO create(ClientProductDTO accountDTO) {
        var account = new Account(
                accountDTO.clientId(),
                accountDTO.productId(),
                BigDecimal.ZERO,
                BigDecimal.valueOf(accountDTO.interestRate()),
                "CC".equals(accountDTO.keyType()) || "NS".equals(accountDTO.keyType()),
                false,
                AccountStatusEnum.ACTIVE
        );
        accountRepository.save(account);
        log.info("СОЗДАН Account: {}", account);
        return mapToDTO(account);
    }

    @Transactional
    @Override
    public Account updateAccountByTransaction(TransactionDTO transactionDTO){
        var account = getAccountById(transactionDTO.getAccountId());
        if(account.getStatus().equals(AccountStatusEnum.ARRESTED)
                || account.getStatus().equals(AccountStatusEnum.BLOCKED)){
            throw new RuntimeException("Аккаунт заблокирован");
        }
        if(transactionDTO.getType().equals(TransactionTypeEnum.DEBITING)){
            account.setBalance(account.getBalance().subtract(transactionDTO.getAmount()));
        } else if(transactionDTO.getType().equals(TransactionTypeEnum.ACCRUAL)){
            account.setBalance(account.getBalance().add(transactionDTO.getAmount()));
        }
        accountRepository.save(account);
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

    @Transactional
    @Override
    public void updateCardExist(Long accountId) {
        var account = getAccountById(accountId);
        account.setCardExist(true);
        accountRepository.save(account);
    }

    @Transactional
    @Override
    public void blockAccount(Account account){
        account.setStatus(AccountStatusEnum.BLOCKED);
        accountRepository.save(account);
    }

    @Transactional
    @Override
    public void debitMoney(Account account, BigDecimal amount){
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
    }
}
