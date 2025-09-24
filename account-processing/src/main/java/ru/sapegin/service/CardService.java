package ru.sapegin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sapegin.dto.CardDTO;
import ru.sapegin.model.Card;
import ru.sapegin.repository.CardRepository;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final AccountService accountService;

    @Transactional
    public CardDTO create(CardDTO cardDTO){
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
        return mapToDTO(card);
    }

    public static String generateCardId(String paymentSystem) {
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

    private static int calculateLuhnCheckDigit(String number) {
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

    public CardDTO mapToDTO(Card card){
        return new CardDTO(
                card.getAccount().getId(),
                card.getCardId(),
                card.getPaymentSystem(),
                card.getStatus()
        );
    }
}
