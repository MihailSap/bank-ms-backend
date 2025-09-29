package ru.sapegin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sapegin.dto.CardDTO;
import ru.sapegin.model.Card;
import ru.sapegin.model.Transaction;
import ru.sapegin.repository.CardRepository;
import ru.sapegin.service.CardServiceI;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardServiceI {

    private final CardRepository cardRepository;
    private final AccountServiceImpl accountService;

    @Transactional
    @Override
    public CardDTO create(CardDTO cardDTO) {
        var accountId = cardDTO.getAccountId();
        var paymentSystem = cardDTO.getPaymentSystem();
        var account = accountService.getAccountById(accountId);
        if(account.getStatus().equals("BLOCKED")){
            throw new RuntimeException("Account status is BLOCKED");
        }

        var card = new Card(
                account,
                generateCardId(paymentSystem),
                paymentSystem
        );

        cardRepository.save(card);
        log.info("СОЗДАНА Card: {}", card);
        accountService.updateCardExist(accountId);
        return mapToDTO(card);
    }

    @Override
    public String generateCardId(String paymentSystem) {
        var random = new Random();
        var cardNumber = new StringBuilder();
        switch (paymentSystem.toUpperCase()) {
            case "VISA" -> cardNumber.append("4");
            case "MASTERCARD" -> cardNumber.append("5");
            case "MIR" -> cardNumber.append("2");
            default -> cardNumber.append("9");
        }
        for (int i = 0; i < 14; i++) {
            cardNumber.append(random.nextInt(10));
        }
        var checkDigit = calculateLuhnCheckDigit(cardNumber.toString());
        cardNumber.append(checkDigit);
        return cardNumber.toString();
    }

    @Override
    public int calculateLuhnCheckDigit(String number) {
        var sum = 0;
        var alternate = true;
        for (int i = number.length() - 1; i >= 0; i--) {
            var n = number.charAt(i) - '0';
            if (alternate) {
                n *= 2;
                if (n > 9){
                    n -= 9;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (10 - (sum % 10)) % 10;
    }

    @Override
    public CardDTO mapToDTO(Card card) {
        return new CardDTO(
                card.getAccount().getId(),
                card.getCardId(),
                card.getPaymentSystem(),
                card.getStatus()
        );
    }

    @Override
    public Card getCardById(Long id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Card с таким id не найдена"));
    }

    public int getTransactionsCountByTime(List<Transaction> transactionsByCardId, int sT, int eT){
        var cnt = 0;
        if(!transactionsByCardId.isEmpty()){
            for(var t : transactionsByCardId){
                var timestamp = t.getTimestamp();
                var startT = LocalDateTime.now().minusDays(sT);
                var endT = LocalDateTime.now().plusDays(eT);
                if(timestamp.isAfter(startT) && timestamp.isBefore(endT)){
                    cnt++;
                }
            }
        }
        return cnt;
    }
}
