package ru.sapegin.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sapegin.model.Account;
import ru.sapegin.model.Payment;
import ru.sapegin.repository.PaymentRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private AccountServiceImpl accountService;

    @Spy
    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    @DisplayName("Получение размера аннуитетного взноса")
    void getATest(){
        BigDecimal S = BigDecimal.valueOf(1200);
        BigDecimal interestRate = BigDecimal.valueOf(12);
        int n = 12;
        BigDecimal expected = BigDecimal.valueOf(106.62);
        BigDecimal result = paymentService.getA(S, interestRate, n);
        assertEquals(0, expected.setScale(2, RoundingMode.HALF_UP)
                        .compareTo(result.setScale(2, RoundingMode.HALF_UP)),
                "Аннуитетный платеж должен совпадать с ожидаемым");
    }

    @Test
    @DisplayName("Получение платежей по кредиту")
    void getCreditPaymentsTest(){
        Account account = new Account();
        account.setId(1L);
        int n = 3;
        BigDecimal A = BigDecimal.valueOf(1000);
        List<Payment> payments = paymentService.getCreditPayments(account, n, A);
        assertEquals(n, payments.size(), "Должно создаться n платежей");
    }

    @Test
    @DisplayName("Получение следующего платежа по кредиту")
    void getNextCreditPaymentIfExists() {
        Account account = new Account();
        account.setId(1L);

        Payment paidPayment = new Payment();
        paidPayment.setId(1L);
        paidPayment.setPayedAt(LocalDate.now());
        paidPayment.setCredit(true);
        paidPayment.setPaymentDate(LocalDate.now().minusDays(1));

        Payment futurePayment = new Payment();
        futurePayment.setId(2L);
        futurePayment.setPayedAt(null);
        futurePayment.setCredit(true);
        futurePayment.setPaymentDate(LocalDate.now().plusDays(1));

        Payment nextPayment = new Payment();
        nextPayment.setId(3L);
        nextPayment.setPayedAt(null);
        nextPayment.setCredit(true);
        nextPayment.setPaymentDate(LocalDate.now().minusDays(1));

        List<Payment> allPayments = List.of(paidPayment, futurePayment, nextPayment);
        when(paymentRepository.findByAccountId(account.getId())).thenReturn(allPayments);
        Optional<Payment> result = paymentService.getNextCreditPaymentIfExists(account);
        assertTrue(result.isPresent(), "Должен вернуться следующий платеж");
        assertEquals(nextPayment, result.get(), "Следующий платеж должен быть неоплаченным, кредитным и с датой <= сегодня");
    }

    @Test
    @DisplayName("Получение неоплаченных платежей")
    void getUnpaidPayments() {
        Account account = new Account();
        account.setId(1L);

        Payment paidPayment = new Payment();
        paidPayment.setId(1L);
        paidPayment.setPayedAt(LocalDate.now().minusDays(1));

        Payment unpaidPayment1 = new Payment();
        unpaidPayment1.setId(2L);
        unpaidPayment1.setPayedAt(null);

        List<Payment> allPayments = List.of(paidPayment, unpaidPayment1);

        when(paymentRepository.findByAccountId(account.getId())).thenReturn(allPayments);

        List<Payment> result = paymentService.getUnpaidPayments(account);
        assertEquals(1, result.size(), "Должны вернуться только неоплаченные платежи");
        assertTrue(result.contains(unpaidPayment1), "Список должен содержать неоплаченный платеж 1");
        assertFalse(result.contains(paidPayment), "Список не должен содержать оплаченный платеж");
    }


    @Test
    @DisplayName("Получение размера долга по кредиту")
    void getDebtAmount(){
        Payment payment1 = new Payment();
        payment1.setAmount(BigDecimal.valueOf(100));
        Payment payment2 = new Payment();
        payment2.setAmount(BigDecimal.valueOf(250.50));
        Payment payment3 = new Payment();
        payment3.setAmount(BigDecimal.valueOf(349.75));
        List<Payment> payments = List.of(payment1, payment2, payment3);

        BigDecimal expected = BigDecimal.valueOf(700.25);
        BigDecimal result = paymentService.getDebtAmount(payments);
        assertEquals(expected, result, "Сумма всех платежей должна совпадать с ожидаемой");
    }
}
