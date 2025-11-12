package ru.sapegin.service;

import io.jsonwebtoken.Claims;
import ru.sapegin.enums.TokenType;

import javax.crypto.SecretKey;

public interface JwtTokenServiceI {

    boolean validateToken(String token, TokenType tokenType);

    Claims getClaims(String token, TokenType tokenType);

    SecretKey getSecretKey(TokenType tokenType);

    SecretKey decodeSecret(String secret);
}
