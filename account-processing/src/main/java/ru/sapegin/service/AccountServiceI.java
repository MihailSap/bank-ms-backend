package ru.sapegin.service;

import ru.sapegin.dto.AccountDTO;
import ru.sapegin.dto.ClientProductDTO;
import ru.sapegin.dto.TransactionDTO;
import ru.sapegin.model.Account;

import java.math.BigDecimal;

public interface AccountServiceI {

    AccountDTO create(ClientProductDTO accountDTO);

    Account updateAccountByTransaction(TransactionDTO transactionDTO);

    AccountDTO mapToDTO(Account account);

    Account getAccountById(Long accountId);

    void updateCardExist(Long accountId);

    void blockAccount(Account account);

    void debitMoney(Account account, BigDecimal amount);
}
