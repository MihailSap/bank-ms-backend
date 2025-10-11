package ru.sapegin.jwt;

import jakarta.security.auth.message.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.sapegin.dto.AuthRequest;
import ru.sapegin.dto.AuthResponse;
import ru.sapegin.model.User;
import ru.sapegin.service.impl.UserServiceImpl;

@Service
public class JwtAuthServiceImpl {

    private final UserServiceImpl userService;
    private final JwtTokenServiceImpl jwtTokenServiceImpl;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public JwtAuthServiceImpl(UserServiceImpl userService, JwtTokenServiceImpl jwtTokenServiceImpl, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtTokenServiceImpl = jwtTokenServiceImpl;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse login(AuthRequest authRequest) throws AuthException {
        User user = userService.getUserByLogin(authRequest.login());
        if (passwordEncoder.matches(authRequest.password(), user.getPassword())) {
            return new AuthResponse(jwtTokenServiceImpl.generateAccessToken(user), "refreshToken");
        } else {
            throw new AuthException("Неправильный пароль");
        }
    }
}
