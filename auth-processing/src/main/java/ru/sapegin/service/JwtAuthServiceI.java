package ru.sapegin.service;

import jakarta.security.auth.message.AuthException;
import ru.sapegin.dto.AuthRequest;
import ru.sapegin.dto.AuthResponse;

public interface JwtAuthServiceI {

    AuthResponse login(AuthRequest authRequest) throws AuthException;

    AuthResponse getAccessToken(String refreshToken);

    AuthResponse refresh(String refreshToken) throws AuthException;

    void logout(String refreshToken);
}