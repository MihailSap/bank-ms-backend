package ru.sapegin.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

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

    private boolean cardExist;

    private String status;

    public Account(Long clientId, Long productId, BigDecimal balance,
                   BigDecimal interestRate, boolean isRecalc, boolean cardExist, String status) {
        this.clientId = clientId;
        this.productId = productId;
        this.balance = balance;
        this.interestRate = interestRate;
        this.isRecalc = isRecalc;
        this.cardExist = cardExist;
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

    public boolean isCardExist() {
        return cardExist;
    }

    public void setCardExist(boolean cardExists) {
        this.cardExist = cardExists;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
