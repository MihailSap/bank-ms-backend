package ru.sapegin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sapegin.model.Account;
import ru.sapegin.model.Payment;
import ru.sapegin.repository.PaymentRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl {

    private final PaymentRepository paymentRepository;

    @Transactional
    public void createCreditPayments(Account account, int n){
        var A = getA(account, n);
        var payments = getCreditPayments(account, n, A);
        saveCreditPayments(payments);
    }

    public BigDecimal getA(Account account, int n){
        var S = account.getBalance();
        var i = account.getInterestRate()
                .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);

        var iPlusOne = BigDecimal.ONE.add(i);
        var iPlusOnePowN = iPlusOne.pow(n);
        var numerator = iPlusOnePowN.multiply(i);
        var denominator = iPlusOnePowN.subtract(BigDecimal.ONE);
        return S.multiply(numerator.divide(denominator, 10, RoundingMode.HALF_UP));
    }

    public List<Payment> getCreditPayments(Account account, int n, BigDecimal A){
        List<Payment> payments = new ArrayList<>();
        var startDate = LocalDate.now().plusMonths(1);
        for(int j = 0; j < n; j++) {
            var payment = new Payment();
            payment.setAccount(account);
            payment.setPaymentDate(startDate.plusMonths(j));
            payment.setAmount(A);
            payment.setCredit(true);
            payment.setType("MONTHLY_CREDIT_PAYMENT");
            payments.add(payment);
        }
        return payments;
    }

    @Transactional
    public void saveCreditPayments(List<Payment> payments){
        paymentRepository.saveAll(payments);
        log.info("СОЗДАНО кредитных Payments: {}", payments.size());
    }
}
