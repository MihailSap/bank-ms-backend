package ru.sapegin.service.impl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.sapegin.enums.TokenType;
import ru.sapegin.service.JwtTokenServiceI;

import javax.crypto.SecretKey;
import java.security.Key;

@Slf4j
@Component
public class JwtTokenServiceImpl implements JwtTokenServiceI {

    @Value("${jwt.secret.access}")
    private String accessSecret;

    @Value("${jwt.secret.refresh}")
    private String refreshSecret;

    @Override
    public boolean validateToken(String token, TokenType tokenType) {
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
