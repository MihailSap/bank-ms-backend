package ru.sapegin.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
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
}
