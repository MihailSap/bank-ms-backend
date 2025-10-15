package ru.sapegin.service;

import ru.sapegin.dto.CardDTO;
import ru.sapegin.model.Card;
import ru.sapegin.model.Transaction;

import java.util.List;

public interface CardServiceI {

    Card create(CardDTO cardDTO);

    String generateCardId(String paymentSystem);

    int calculateLuhnCheckDigit(String number);

    CardDTO mapToDTO(Card card);

    Card getCardById(Long id);

    int getTransactionsCountByTime(List<Transaction> transactionsByCardId, int sT, int eT);
}
