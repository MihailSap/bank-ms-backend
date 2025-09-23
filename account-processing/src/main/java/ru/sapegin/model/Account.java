package ru.sapegin.model;

import jakarta.persistence.*;
import ru.sapegin.enums.StatusEnum;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long clientId;

    private Long productId;

    private BigDecimal balance;

    private BigDecimal interestRate;

    private boolean isRecalc;

    private boolean cardExists;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    public Account(Long id, Long clientId, Long productId, BigDecimal balance,
                   BigDecimal interestRate, boolean isRecalc, boolean cardExists, StatusEnum status) {
        this.id = id;
        this.clientId = clientId;
        this.productId = productId;
        this.balance = balance;
        this.interestRate = interestRate;
        this.isRecalc = isRecalc;
        this.cardExists = cardExists;
        this.status = status;
    }

    public Account() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public boolean isRecalc() {
        return isRecalc;
    }

    public void setRecalc(boolean recalc) {
        isRecalc = recalc;
    }

    public boolean isCardExists() {
        return cardExists;
    }

    public void setCardExists(boolean cardExists) {
        this.cardExists = cardExists;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }
}
