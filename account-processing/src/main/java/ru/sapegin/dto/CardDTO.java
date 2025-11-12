package ru.sapegin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.sapegin.enums.CardStatusEnum;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CardDTO {

    private Long accountId;

    private String cardId;

    private String paymentSystem;

    private CardStatusEnum status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardDTO cardDTO = (CardDTO) o;
        return Objects.equals(accountId, cardDTO.accountId) && Objects.equals(cardId, cardDTO.cardId) && Objects.equals(paymentSystem, cardDTO.paymentSystem) && status == cardDTO.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, cardId, paymentSystem, status);
    }
}
