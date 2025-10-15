package ru.sapegin.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sapegin.dto.CardDTO;
import ru.sapegin.enums.AccountStatusEnum;
import ru.sapegin.enums.CardStatusEnum;
import ru.sapegin.model.Account;
import ru.sapegin.model.Card;
import ru.sapegin.model.Transaction;
import ru.sapegin.repository.CardRepository;

import java.time.LocalDateTime;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardServiceImplTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private AccountServiceImpl accountService;

    @Spy
    @InjectMocks
    private CardServiceImpl cardService;

    @Test
    @DisplayName("Успешное создание Card")
    void createTest() {
        var cardDTO = new CardDTO();
        cardDTO.setAccountId(1L);
        cardDTO.setPaymentSystem("VISA");

        var account = new Account();
        account.setId(1L);
        account.setStatus(AccountStatusEnum.ACTIVE);

        when(accountService.getAccountById(1L)).thenReturn(account);
        when(cardRepository.save(Mockito.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));
        doReturn("1234-5678-9012-3456").when(cardService).generateCardId("VISA");

        Card result = cardService.create(cardDTO);
        Card expectedCard = new Card(account, "1234-5678-9012-3456", "VISA");
        assertEquals(result, expectedCard);
    }

    @Test
    @DisplayName("Попытка создать Card для заблокированного Account")
    void createWithBlockedAccountTest() {
        var cardDTO = new CardDTO();
        cardDTO.setAccountId(1L);
        cardDTO.setPaymentSystem("VISA");

        var blockedAccount = new Account();
        blockedAccount.setId(1L);
        blockedAccount.setStatus(AccountStatusEnum.BLOCKED);

        when(accountService.getAccountById(1L)).thenReturn(blockedAccount);
        Exception exception = assertThrows(RuntimeException.class, () -> cardService.create(cardDTO));
        assertEquals("Аккаунт заблокирован, создание карты недоступно", exception.getMessage());
    }

    @Test
    @DisplayName("Маппинг Card в CardDTO")
    void mapToDTOTest(){
        var account = new Account();
        account.setId(1L);

        var card = new Card();
        card.setAccount(account);
        card.setCardId("1234-5678-9012-3456");
        card.setPaymentSystem("VISA");
        card.setStatus(CardStatusEnum.ACTIVE);

        var expectedDTO = new CardDTO(
                1L,
                "1234-5678-9012-3456",
                "VISA",
                CardStatusEnum.ACTIVE
        );

        var result = cardService.mapToDTO(card);
        assertEquals(expectedDTO, result);
    }

    @Test
    @DisplayName("Получение количества транзакций за определенное время")
    void getTransactionsCountByTimeTest() {
        var now = LocalDateTime.now();
        var t1 = new Transaction();
        t1.setTimestamp(now.minusDays(1));
        var t2 = new Transaction();
        t2.setTimestamp(now.minusDays(3));
        var t3 = new Transaction();
        t3.setTimestamp(now.plusDays(1));
        var t4 = new Transaction();
        t4.setTimestamp(now.plusDays(5));

        List<Transaction> transactions = List.of(t1, t2, t3, t4);
        int startDays = 2;
        int endDays = 2;
        int count = cardService.getTransactionsCountByTime(transactions, startDays, endDays);
        assertEquals(2, count);
    }


    @Test
    @DisplayName("Создание cardId")
    void generateCardIdTest(){
        String paymentSystem = "VISA";
        String cardId = cardService.generateCardId(paymentSystem);
        assertEquals(16, cardId.length());
        assertTrue(cardId.matches("\\d+"));
        assertEquals('4', cardId.charAt(0));
    }

    @Test
    @DisplayName("Вычисление контрольной цифры")
    void calculateLuhnCheckDigitTest(){
        String numberWithoutCheckDigit = "7992739871";
        int resultDigit = cardService.calculateLuhnCheckDigit(numberWithoutCheckDigit);
        int expectedDigit = 3;
        assertEquals(expectedDigit, resultDigit);
    }
}
