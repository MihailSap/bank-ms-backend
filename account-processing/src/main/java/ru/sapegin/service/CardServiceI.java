package ru.sapegin.service;

import ru.sapegin.dto.CardDTO;
import ru.sapegin.model.Card;

public interface CardServiceI {

    CardDTO create(CardDTO cardDTO);

    String generateCardId(String paymentSystem);

    int calculateLuhnCheckDigit(String number);

    CardDTO mapToDTO(Card card);

    Card getCardById(Long id);
}
