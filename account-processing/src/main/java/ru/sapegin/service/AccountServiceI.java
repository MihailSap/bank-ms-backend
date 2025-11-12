package ru.sapegin.service;

import ru.sapegin.dto.AccountDTO;
import ru.sapegin.dto.ClientProductDTO;
import ru.sapegin.dto.TransactionDTO;
import ru.sapegin.model.Account;

import java.math.BigDecimal;

public interface AccountServiceI {

    Account create(ClientProductDTO accountDTO);

    Account updateAccountByTransaction(TransactionDTO transactionDTO);

    AccountDTO mapToDTO(Account account);

    Account getAccountById(Long accountId);

    Account updateCardExist(Long accountId);

    void blockAccount(Account account);

    void debitMoney(Account account, BigDecimal amount);

    void accrualMoney(Account account , BigDecimal amount);
}
