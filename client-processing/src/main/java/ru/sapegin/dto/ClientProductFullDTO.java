package ru.sapegin.dto;

import ru.sapegin.enums.StatusEnum;

import java.time.LocalDate;

public record ClientProductFullDTO(Long clientId, Long productId, LocalDate openDate, StatusEnum status) {
}
