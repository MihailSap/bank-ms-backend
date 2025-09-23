package ru.sapegin.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "product_registry")
public class ProductRegistry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_registry_id", nullable = false)
    private ProductRegistry productRegistry;

    private LocalDateTime paymentDate;

    private BigDecimal amount;

    private BigDecimal interestRateAmount;

    private BigDecimal debtRateAmount;

    private boolean expired;

    private LocalDateTime paymentExpirationDate;

    public ProductRegistry(Long id, ProductRegistry productRegistry, LocalDateTime paymentDate,
                           BigDecimal amount, BigDecimal interestRateAmount, BigDecimal debtRateAmount,
                           boolean expired, LocalDateTime paymentExpirationDate) {
        this.id = id;
        this.productRegistry = productRegistry;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.interestRateAmount = interestRateAmount;
        this.debtRateAmount = debtRateAmount;
        this.expired = expired;
        this.paymentExpirationDate = paymentExpirationDate;
    }

    public ProductRegistry() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductRegistry getProductRegistry() {
        return productRegistry;
    }

    public void setProductRegistry(ProductRegistry productRegistry) {
        this.productRegistry = productRegistry;
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

    public BigDecimal getInterestRateAmount() {
        return interestRateAmount;
    }

    public void setInterestRateAmount(BigDecimal interestRateAmount) {
        this.interestRateAmount = interestRateAmount;
    }

    public BigDecimal getDebtRateAmount() {
        return debtRateAmount;
    }

    public void setDebtRateAmount(BigDecimal debtRateAmount) {
        this.debtRateAmount = debtRateAmount;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public LocalDateTime getPaymentExpirationDate() {
        return paymentExpirationDate;
    }

    public void setPaymentExpirationDate(LocalDateTime paymentExpirationDate) {
        this.paymentExpirationDate = paymentExpirationDate;
    }
}
