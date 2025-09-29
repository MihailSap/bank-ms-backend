package ru.sapegin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.sapegin.enums.KeyEnum;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientProductKeyDTO {

    private Long clientId;

    private Long productId;

    private KeyEnum keyType;
}
