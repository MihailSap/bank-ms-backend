package ru.sapegin.service;

import java.math.BigDecimal;

public interface CreditServiceI {

    boolean canClientOpenCredit(Long clientId);

    BigDecimal getClientDebt(Long clientId);

    boolean hasExpiredPayment(Long clientId);

    BigDecimal getCurrentCreditAmount();
}
