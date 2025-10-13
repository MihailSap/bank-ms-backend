package ru.sapegin.service.impl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.sapegin.enums.TokenType;
import ru.sapegin.model.User;
import ru.sapegin.service.JwtTokenServiceI;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenServiceImpl implements JwtTokenServiceI {

    @Value("${jwt.secret.access}")
    private String accessSecret;

    @Value("${jwt.secret.refresh}")
    private String refreshSecret;

    @Override
    public String getAccessToken(User user) {
        var accessSecretKey = decodeSecret(accessSecret);
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusMinutes(1).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);
        return Jwts.builder()
                .setSubject(user.getLogin())
                .setExpiration(accessExpiration)
                .signWith(accessSecretKey)
                .claim("role", user.getRole())
                .claim("login", user.getLogin())
                .compact();
    }

    @Override
    public String getRefreshToken(User user) {
        SecretKey refreshSecretKey = decodeSecret(refreshSecret);
        LocalDateTime now = LocalDateTime.now();
        Instant refreshExpirationInstant = now.plusDays(30).atZone(ZoneId.systemDefault()).toInstant();
        Date refreshExpiration = Date.from(refreshExpirationInstant);
        return Jwts.builder()
                .setSubject(user.getLogin())
                .setExpiration(refreshExpiration)
                .signWith(refreshSecretKey)
                .compact();
    }

    @Override
    public boolean validateRefreshToken(String token, TokenType tokenType) {
        Key secretKey = getSecretKey(tokenType);
        try{
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error("Некорректный токен", e);
        }
        return false;
    }

    @Override
    public Claims getClaims(String token, TokenType tokenType) {
        Key secretKey = getSecretKey(tokenType);
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public SecretKey getSecretKey(TokenType tokenType) {
        if(tokenType.equals(TokenType.ACCESS)){
            return decodeSecret(accessSecret);
        }
        return decodeSecret(refreshSecret);
    }

    @Override
    public SecretKey decodeSecret(String secret){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }
}
