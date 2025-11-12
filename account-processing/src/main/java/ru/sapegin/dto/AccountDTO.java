package ru.sapegin.dto;

import java.math.BigDecimal;

public record AccountDTO(Long clientId, Long productId, BigDecimal balance,
                         BigDecimal interestRate, boolean isRecalc, boolean cardExists, String status) {
}
