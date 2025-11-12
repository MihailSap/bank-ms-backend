package ru.sapegin.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {

    private Long accountId;

    private Long cardId;

    String type;

    BigDecimal amount;

    String status;

    LocalDateTime timestamp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionDTO that = (TransactionDTO) o;
        return Objects.equals(accountId, that.accountId) && Objects.equals(cardId, that.cardId) && Objects.equals(type, that.type) && Objects.equals(amount, that.amount) && Objects.equals(status, that.status) && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, cardId, type, amount, status, timestamp);
    }
}
