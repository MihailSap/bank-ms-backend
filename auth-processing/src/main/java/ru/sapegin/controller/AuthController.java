package ru.sapegin.controller;

import jakarta.security.auth.message.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.sapegin.dto.AuthRequest;
import ru.sapegin.dto.AuthResponse;
import ru.sapegin.dto.UserDTO;
import ru.sapegin.model.User;
import ru.sapegin.service.impl.JwtAuthServiceImpl;
import ru.sapegin.service.impl.UserServiceImpl;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtAuthServiceImpl jwtAuthServiceImpl;
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public AuthController(JwtAuthServiceImpl jwtAuthServiceImpl, UserServiceImpl userServiceImpl) {
        this.jwtAuthServiceImpl = jwtAuthServiceImpl;
        this.userServiceImpl = userServiceImpl;
    }

    @PostMapping("/register")
    public UserDTO register(@RequestBody UserDTO userDTO){
        User user = userServiceImpl.create(userDTO);
        return userServiceImpl.mapToDTO(user);
    }

    @PostMapping("/sign-up")
    public AuthResponse login(@RequestBody AuthRequest authRequest) throws AuthException {
        return jwtAuthServiceImpl.login(authRequest);
    }

    @PostMapping("/refresh")
    public AuthResponse getNewRefreshToken(@RequestBody AuthResponse auth) throws AuthException {
        return jwtAuthServiceImpl.refresh(auth.refreshToken());
    }

    @PostMapping("/logout")
    public void deleteRefreshToken(@RequestBody AuthResponse request) {
        jwtAuthServiceImpl.logout(request.refreshToken());
    }
}
