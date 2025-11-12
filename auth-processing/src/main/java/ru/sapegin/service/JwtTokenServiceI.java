package ru.sapegin.service;

import io.jsonwebtoken.Claims;
import ru.sapegin.enums.TokenType;
import ru.sapegin.model.User;

import javax.crypto.SecretKey;

public interface JwtTokenServiceI {

    String getAccessToken(User user);

    String getRefreshToken(User user);

    boolean validateRefreshToken(String token, TokenType tokenType);

    Claims getClaims(String token, TokenType tokenType);

    SecretKey getSecretKey(TokenType tokenType);

    SecretKey decodeSecret(String secret);
}
