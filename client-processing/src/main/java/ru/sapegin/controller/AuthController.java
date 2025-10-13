package ru.sapegin.controller;

import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import ru.sapegin.dto.AuthRequest;
import ru.sapegin.dto.AuthResponse;
import ru.sapegin.service.impl.JwtAuthServiceImpl;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtAuthServiceImpl jwtAuthServiceImpl;

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
}
