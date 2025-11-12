package ru.sapegin.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "card")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    private String cardId;

    private String paymentSystem;

    private String status;

    public Card(Account account, String cardId, String paymentSystem) {
        this.account = account;
        this.cardId = cardId;
        this.paymentSystem = paymentSystem;
        this.status = "ACTIVE";
    }
}
