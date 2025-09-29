package ru.sapegin.dto;

import lombok.*;
import ru.sapegin.enums.TransactionStatusEnum;
import ru.sapegin.enums.TransactionTypeEnum;

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

    TransactionTypeEnum type;

    BigDecimal amount;

    TransactionStatusEnum status;

    LocalDateTime timestamp;
}
