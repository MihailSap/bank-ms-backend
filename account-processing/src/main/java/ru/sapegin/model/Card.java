package ru.sapegin.model;

import jakarta.persistence.*;
import lombok.*;
import ru.sapegin.enums.CardStatusEnum;

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

    @Enumerated(EnumType.STRING)
    private CardStatusEnum status;

    public Card(Account account, String cardId, String paymentSystem) {
        this.account = account;
        this.cardId = cardId;
        this.paymentSystem = paymentSystem;
        this.status = CardStatusEnum.ACTIVE;
    }
}
