package ru.sapegin.dto;

public record AuthResponse(String jwtAccessToken, String refreshToken) {
}
