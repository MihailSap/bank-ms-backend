package ru.sapegin.controller;

import jakarta.security.auth.message.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest authRequest) throws AuthException {
        return jwtAuthServiceImpl.login(authRequest);
    }

    @PostMapping("/token")
    public AuthResponse getNewAccessToken(@RequestBody AuthResponse auth) {
        return jwtAuthServiceImpl.getAccessToken(auth.refreshToken());
    }

    @PostMapping("/refresh")
    public AuthResponse getNewRefreshToken(@RequestBody AuthResponse auth) throws AuthException {
        return jwtAuthServiceImpl.refresh(auth.refreshToken());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/role")
    public String getUserRole(Authentication authentication) {
        String username = authentication.getName();
        String role = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("UNKNOWN");

        return String.format("Привет, %s! Твоя роль: %s", username, role);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> deleteRefreshToken(@RequestBody AuthResponse request) throws AuthException {
        jwtAuthServiceImpl.logout(request.refreshToken());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public UserDTO register(@RequestBody UserDTO userDTO){
        User user = userServiceImpl.create(userDTO);
        return userServiceImpl.mapToDTO(user);
    }
}
