package ru.sapegin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sapegin.dto.ClientProductDTO;
import ru.sapegin.model.Account;
import ru.sapegin.repository.AccountRepository;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional
    public void create(ClientProductDTO accountDTO){
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
    }
}
