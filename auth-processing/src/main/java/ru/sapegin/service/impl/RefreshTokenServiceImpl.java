package ru.sapegin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sapegin.model.RefreshToken;
import ru.sapegin.model.User;
import ru.sapegin.repository.RefreshTokenRepository;
import ru.sapegin.service.RefreshTokenServiceI;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenServiceI {

    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public RefreshToken getByUser(User user){
        return refreshTokenRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Refresh токен данного пользователя не найден"));
    }

    @Override
    public RefreshToken getByBody(String body){
        return refreshTokenRepository.findByBody(body)
                .orElseThrow(() -> new RuntimeException("Токен с таким содержимым не найден"));
    }

    @Transactional
    @Override
    public void save(RefreshToken existingToken) {
        refreshTokenRepository.save(existingToken);
    }
}
