package ru.sapegin.dto;

public record ClientProductDTO(Long clientId, Long productId, int interestRate, String keyType) {
}
