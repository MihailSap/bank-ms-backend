package ru.sapegin.service.impl;


import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sapegin.dto.AuthRequest;
import ru.sapegin.dto.AuthResponse;
import ru.sapegin.enums.TokenType;
import ru.sapegin.model.User;
import ru.sapegin.service.JwtAuthServiceI;

@Service
public class JwtAuthServiceImpl implements JwtAuthServiceI {

    private final RefreshTokenServiceImpl refreshTokenService;
    private final UserServiceImpl userService;
    private final JwtTokenServiceImpl jwtTokenService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public JwtAuthServiceImpl(UserServiceImpl userService,
                              JwtTokenServiceImpl jwtTokenService,
                              PasswordEncoder passwordEncoder,
                              RefreshTokenServiceImpl refreshTokenService) {
        this.userService = userService;
        this.jwtTokenService = jwtTokenService;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    @Override
    public AuthResponse login(AuthRequest authRequest) throws AuthException {
        User user = userService.getUserByLogin(authRequest.login());
        if (passwordEncoder.matches(authRequest.password(), user.getPassword())) {
            String accessToken = jwtTokenService.getAccessToken(user);
            String refreshToken = jwtTokenService.getRefreshToken(user);
            userService.updateRefreshToken(user, refreshToken);
            return new AuthResponse(accessToken, refreshToken);
        } else {
            throw new AuthException("Неправильный пароль");
        }
    }

    @Override
    public AuthResponse getAccessToken(String refreshToken) {
        if (jwtTokenService.validateToken(refreshToken, TokenType.REFRESH)) {
            Claims claims = jwtTokenService.getClaims(refreshToken, TokenType.REFRESH);
            String login = claims.getSubject();
            User user = userService.getUserByLogin(login);
            var saveRefreshToken = refreshTokenService.getByUser(user).getBody();
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                String accessToken = jwtTokenService.getAccessToken(user);
                return new AuthResponse(accessToken, null);
            }
        }
        return new AuthResponse(null, null);
    }

    @Override
    public AuthResponse refresh(String refreshToken) throws AuthException {
        if (jwtTokenService.validateToken(refreshToken, TokenType.REFRESH)) {
            Claims claims = jwtTokenService.getClaims(refreshToken, TokenType.REFRESH);
            String login = claims.getSubject();
            User user = userService.getUserByLogin(login);
            var saveRefreshToken = refreshTokenService.getByUser(user).getBody();
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                String accessToken = jwtTokenService.getAccessToken(user);
                String newRefreshToken = jwtTokenService.getRefreshToken(user);
                var existingToken = refreshTokenService.getByUser(user);
                existingToken.setBody(newRefreshToken);
                refreshTokenService.save(existingToken);
                return new AuthResponse(accessToken, newRefreshToken);
            }
        }
        throw new AuthException("Невалидный JWT токен");
    }

    @Override
    public void logout(String refreshToken) {
        var token = refreshTokenService.getByBody(refreshToken);
        var user = token.getUser();
        userService.updateRefreshToken(user, null);
    }
}
