package ru.sapegin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.sapegin.enums.CardStatusEnum;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CardDTO {

    private Long accountId;

    private String cardId;

    private String paymentSystem;

    private CardStatusEnum status;
}
