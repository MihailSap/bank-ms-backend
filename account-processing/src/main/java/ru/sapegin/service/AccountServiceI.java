package ru.sapegin.service;

import ru.sapegin.dto.AccountDTO;
import ru.sapegin.dto.ClientProductDTO;
import ru.sapegin.model.Account;

public interface AccountServiceI {

    AccountDTO create(ClientProductDTO accountDTO);

    AccountDTO mapToDTO(Account account);

    Account getAccountById(Long accountId);
}
