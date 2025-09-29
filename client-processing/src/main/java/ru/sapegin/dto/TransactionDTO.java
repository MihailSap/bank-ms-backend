package ru.sapegin.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
}
