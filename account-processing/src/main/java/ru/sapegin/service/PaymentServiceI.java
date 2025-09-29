package ru.sapegin.service;

import ru.sapegin.dto.PaymentDTO;
import ru.sapegin.model.Account;
import ru.sapegin.model.Payment;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PaymentServiceI {

    void createCreditPayments(Account account, int n, BigDecimal creditAmount);

    BigDecimal getA(BigDecimal S, BigDecimal interestRate, int n);

    List<Payment> getCreditPayments(Account account, int n, BigDecimal A);

    void saveCreditPayments(List<Payment> payments);

    Optional<Payment> getNextCreditPaymentIfExists(Account account);

    boolean isPaymentExist(Account account);

    void closeCredit(PaymentDTO paymentDTO);

    List<Payment> getUnpaidPayments(Account account);

    BigDecimal getDebtAmount(List<Payment> payments);

    void closeAllPayments(List<Payment> payments);
}
