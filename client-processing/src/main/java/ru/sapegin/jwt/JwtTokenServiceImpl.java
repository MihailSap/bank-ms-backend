package ru.sapegin.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.sapegin.model.User;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenServiceImpl {

    @Value("${jwt.secret.access}")
    private String accessSecret;

    public String generateAccessToken(User user) {
        var accessSecretKey = getSecretKey(accessSecret);
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);
        return Jwts.builder()
                .setSubject(user.getLogin())
                .setExpiration(accessExpiration)
                .signWith(accessSecretKey)
                .claim("role", user.getRole())
                .claim("login", user.getLogin())
                .compact();
    }

    public boolean validateToken(String token) {
        Key secretKey = getSecretKey(accessSecret);
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

    private Claims getClaims(String token, Key secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Claims getAccessClaims(String token) {
        var accessSecretKey = getSecretKey(accessSecret);
        return getClaims(token, accessSecretKey);
    }

    public SecretKey getSecretKey(String secret){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }
}
