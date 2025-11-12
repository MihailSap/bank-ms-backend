package ru.sapegin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sapegin.model.PaymentRegistry;
import ru.sapegin.model.ProductRegistry;
import ru.sapegin.repository.PaymentRegistryRepository;
import ru.sapegin.repository.ProductRegistryRepository;
import ru.sapegin.service.impl.CreditServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreditServiceTest {

    @Mock
    private ProductRegistryRepository productRegistryRepository;

    @Mock
    private PaymentRegistryRepository paymentRegistryRepository;

    @Spy
    @InjectMocks
    private CreditServiceImpl creditService;

    @Test
    @DisplayName("Получение суммы долга клиента")
    void getClientDebtTest(){
        Long clientId = 1L;
        var pr1 = new ProductRegistry();
        pr1.setId(10L);
        var pr2 = new ProductRegistry();
        pr2.setId(20L);

        when(productRegistryRepository.findByClientId(clientId))
                .thenReturn(List.of(pr1, pr2));

        var payment1 = new PaymentRegistry();
        payment1.setPaymentDate(LocalDate.of(2024, 1, 10));
        payment1.setDebtAmount(new BigDecimal("1000.00"));
        payment1.setInterestRateAmount(new BigDecimal("100.00"));
        var payment2 = new PaymentRegistry();
        payment2.setPaymentDate(LocalDate.of(2024, 2, 10));
        payment2.setDebtAmount(new BigDecimal("500.00"));
        payment2.setInterestRateAmount(new BigDecimal("50.00"));

        when(paymentRegistryRepository.findByProductRegistryId(10L))
                .thenReturn(List.of(payment1, payment2));

        var payment3 = new PaymentRegistry();
        payment3.setPaymentDate(LocalDate.of(2024, 3, 15));
        payment3.setDebtAmount(new BigDecimal("2000.00"));
        payment3.setInterestRateAmount(new BigDecimal("200.00"));

        when(paymentRegistryRepository.findByProductRegistryId(20L))
                .thenReturn(List.of(payment3));

        BigDecimal result = creditService.getClientDebt(clientId);
        assertEquals(new BigDecimal("2750.00"), result);
    }

    @Test
    @DisplayName("Имеются ли просроченные платежи у клиента")
    void hasExpiredPaymentTest() {
        Long clientId = 1L;
        var pr1 = new ProductRegistry();
        pr1.setId(10L);

        when(productRegistryRepository.findByClientId(clientId))
                .thenReturn(List.of(pr1));

        var payment1 = new PaymentRegistry();
        payment1.setExpired(false);
        var payment2 = new PaymentRegistry();
        payment2.setExpired(true);

        when(paymentRegistryRepository.findByProductRegistryId(10L))
                .thenReturn(List.of(payment1, payment2));

        boolean result = creditService.hasExpiredPayment(clientId);
        assertEquals(true, result);
    }
}
