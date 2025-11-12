package ru.sapegin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payment_registry")
public class PaymentRegistry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_registry_id", nullable = false)
    private ProductRegistry productRegistry;

    private LocalDate paymentDate;

    private BigDecimal amount;

    private BigDecimal interestRateAmount;

    private BigDecimal debtAmount;

    private boolean expired;

    private LocalDateTime paymentExpirationDate;

    private int monthCount;
}
