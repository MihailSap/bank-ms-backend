package ru.sapegin.model;

import jakarta.persistence.*;
import ru.sapegin.enums.StatusEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    private LocalDateTime paymentDate;

    private BigDecimal amount;

    private boolean isCredit;

    private LocalDateTime payedAt;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    public Payment(Long id, Account account, LocalDateTime paymentDate, BigDecimal amount,
                   boolean isCredit, LocalDateTime payedAt, StatusEnum status) {
        this.id = id;
        this.account = account;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.isCredit = isCredit;
        this.payedAt = payedAt;
        this.status = status;
    }

    public Payment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public boolean isCredit() {
        return isCredit;
    }

    public void setCredit(boolean credit) {
        isCredit = credit;
    }

    public LocalDateTime getPayedAt() {
        return payedAt;
    }

    public void setPayedAt(LocalDateTime payedAt) {
        this.payedAt = payedAt;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }
}
