package ru.sapegin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sapegin.dto.PaymentDTO;
import ru.sapegin.enums.PaymentTypeEnum;
import ru.sapegin.model.Account;
import ru.sapegin.model.Payment;
import ru.sapegin.repository.PaymentRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl {

    private final PaymentRepository paymentRepository;
    private final AccountServiceImpl accountService;

    @Transactional
    public void createCreditPayments(Account account, int n, BigDecimal creditAmount) {
        var A = getA(creditAmount, account.getInterestRate(), n);
        var payments = getCreditPayments(account, n, A);
        saveCreditPayments(payments);
    }

    public BigDecimal getA(BigDecimal S, BigDecimal interestRate, int n){
        var i = interestRate
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
            payment.setType(PaymentTypeEnum.MONTHLY_CREDIT_PAYMENT);
            payments.add(payment);
        }
        return payments;
    }

    @Transactional
    public void saveCreditPayments(List<Payment> payments){
        paymentRepository.saveAll(payments);
        log.info("СОЗДАНО кредитных Payments: {}", payments.size());
    }

    public Optional<Payment> getNextCreditPaymentIfExists(Account account){
        var payments = paymentRepository.findByAccountId(account.getId());
        for(var payment : payments){
            if(payment.getPayedAt() == null  && payment.isCredit()
                    && !payment.getPaymentDate().isAfter(LocalDate.now())){
                return Optional.of(payment);
            }
        }
        return Optional.empty();
    }

    public boolean isPaymentExist(Account account){
        return paymentRepository.findByAccountId(account.getId()).isEmpty();
    }

    @Transactional
    public void closeCredit(PaymentDTO paymentDTO){
        if(paymentDTO.getType().equals(PaymentTypeEnum.ACCRUAL)) {
            var account = accountService.getAccountById(paymentDTO.getAccountId());
            var unpaidPayments = getUnpaidPayments(account);
            var debtAmount = getDebtAmount(unpaidPayments);
            if(paymentDTO.getAmount().equals(debtAmount)){
                accountService.debitMoney(account, debtAmount);
                closeAllPayments(unpaidPayments);
            }
        }
    }

    public List<Payment> getUnpaidPayments(Account account){
        return paymentRepository.findByAccountId(account.getId())
                .stream()
                .filter(p -> p.getPayedAt() == null)
                .toList();
    }

    public BigDecimal getDebtAmount(List<Payment> payments){
        return payments.stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional
    public void closeAllPayments(List<Payment> payments){
        payments.forEach(p -> p.setPayedAt(LocalDate.now()));
        paymentRepository.saveAll(payments);
    }
}
