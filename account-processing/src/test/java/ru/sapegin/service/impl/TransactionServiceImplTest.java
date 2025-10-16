package ru.sapegin.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sapegin.dto.TransactionDTO;
import ru.sapegin.enums.TransactionStatusEnum;
import ru.sapegin.enums.TransactionTypeEnum;
import ru.sapegin.model.Account;
import ru.sapegin.model.Card;
import ru.sapegin.model.Transaction;
import ru.sapegin.repository.PaymentRepository;
import ru.sapegin.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

    @Mock
    private CardServiceImpl cardService;

    @Mock
    private AccountServiceImpl accountService;

    @Mock
    private PaymentServiceImpl paymentService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Spy
    @InjectMocks
    private TransactionServiceImpl transactionServiceImpl;

    @Test
    @DisplayName("Создание Transaction")
    void createTest(){
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setType(TransactionTypeEnum.ACCRUAL);
        transactionDTO.setAmount(BigDecimal.valueOf(500));

        Card card = new Card();
        card.setId(1L);

        Account account = new Account();
        account.setId(1L);

        when(transactionRepository.save(Mockito.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));
        Transaction result = transactionServiceImpl.create(transactionDTO, card, account);

        assertEquals(card, result.getCard());
        assertEquals(account, result.getAccount());
        assertEquals(transactionDTO.getType(), result.getType());
        assertEquals(0, transactionDTO.getAmount().compareTo(result.getAmount()));
        assertEquals(TransactionStatusEnum.ALLOWED, result.getStatus());
        assertNotNull(result.getTimestamp());

        verify(transactionRepository).save(result);
    }

    @Test
    @DisplayName("Маппинг Transaction в TransactionDTO")
    void mapToDTOTest(){
        Card card = new Card();
        card.setId(1L);

        Account account = new Account();
        account.setId(10L);

        Transaction transaction = new Transaction();
        transaction.setCard(card);
        transaction.setAccount(account);
        transaction.setType(TransactionTypeEnum.ACCRUAL);
        transaction.setAmount(BigDecimal.valueOf(500));
        transaction.setStatus(TransactionStatusEnum.ALLOWED);
        transaction.setTimestamp(LocalDateTime.now());

        TransactionDTO result = transactionServiceImpl.mapToDTO(transaction);
        var dto = new TransactionDTO(card.getId(), account.getId(), TransactionTypeEnum.ACCRUAL,
                BigDecimal.valueOf(500), TransactionStatusEnum.ALLOWED, transaction.getTimestamp());

        assertEquals(dto, result);
    }

    @Test
    @DisplayName("Блокировка транзакции")
    void blockTransactionTest(){
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setStatus(TransactionStatusEnum.ALLOWED);

        when(transactionRepository.save(Mockito.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        transactionServiceImpl.blockTransaction(transaction);
        assertEquals(TransactionStatusEnum.BLOCKED, transaction.getStatus());
        verify(transactionRepository).save(transaction);
    }
}
