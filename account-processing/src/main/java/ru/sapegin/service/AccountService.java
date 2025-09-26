package ru.sapegin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sapegin.dto.AccountDTO;
import ru.sapegin.dto.ClientProductDTO;
import ru.sapegin.model.Account;
import ru.sapegin.repository.AccountRepository;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional
    public AccountDTO create(ClientProductDTO accountDTO){
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
        log.info("СОЗДАН Account: {}", account);
        return mapToDTO(account);
    }

    public AccountDTO mapToDTO(Account account){
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

    public Account getAccountById(Long accountId){
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account с таким id не найден"));
    }
}
