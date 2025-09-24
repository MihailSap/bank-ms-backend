package ru.sapegin.dto;

import ru.sapegin.enums.KeyEnum;

public record ClientProductDTO(Long clientId, Long productId, KeyEnum key) {
}
