package ru.sapegin.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.sapegin.enums.Role;
import ru.sapegin.enums.TokenType;
import ru.sapegin.service.impl.JwtTokenServiceImpl;

import java.io.IOException;

@Slf4j
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenServiceImpl jwtTokenServiceImpl;

    @Autowired
    public JwtTokenFilter(JwtTokenServiceImpl jwtTokenServiceImpl) {
        this.jwtTokenServiceImpl = jwtTokenServiceImpl;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getTokenFromRequest(request);
        if (token != null && jwtTokenServiceImpl.validateToken(token, TokenType.ACCESS)) {
            Claims claims = jwtTokenServiceImpl.getClaims(token, TokenType.ACCESS);
            JwtAuthentication jwtInfoToken = getJwtAuthentication(claims);
            jwtInfoToken.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(jwtInfoToken);
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final String bearer = request.getHeader("Authorization");
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

    public JwtAuthentication getJwtAuthentication(Claims claims) {
        JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setRole(Role.CURRENT_CLIENT);
        jwtInfoToken.setLogin(claims.getSubject());
        return jwtInfoToken;
    }
}
