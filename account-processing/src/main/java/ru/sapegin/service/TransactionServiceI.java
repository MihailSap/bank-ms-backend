package ru.sapegin.service;

import ru.sapegin.dto.TransactionDTO;
import ru.sapegin.model.Account;
import ru.sapegin.model.Card;
import ru.sapegin.model.Transaction;

public interface TransactionServiceI {

    Transaction create(TransactionDTO transactionDTO, Card card, Account account);

    TransactionDTO proccessGetTransaction(TransactionDTO transactionDTO);

    TransactionDTO mapToDTO(Transaction transaction);

    void checkTransactionsCount(Card card, Account account, Transaction transaction);

    void blockTransaction(Transaction transaction);
}
