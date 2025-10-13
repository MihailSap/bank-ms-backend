package ru.sapegin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sapegin.model.TokenBlacklist;
import ru.sapegin.repository.TokenBlacklistRepository;
import ru.sapegin.service.TokenBlacklistServiceI;

@Service
public class TokenBlacklistServiceImpl implements TokenBlacklistServiceI {

    private final TokenBlacklistRepository tokenBlacklistRepository;

    @Autowired
    public TokenBlacklistServiceImpl(TokenBlacklistRepository tokenBlacklistRepository) {
        this.tokenBlacklistRepository = tokenBlacklistRepository;
    }

    @Transactional
    @Override
    public void add(String body){
        TokenBlacklist tokenBlacklist = new TokenBlacklist(body);
        tokenBlacklistRepository.save(tokenBlacklist);
    }

    @Transactional
    @Override
    public void remove(Long id){
        TokenBlacklist token = getById(id);
        tokenBlacklistRepository.delete(token);
    }

    @Override
    public boolean contains(String tokenBody){
        return tokenBlacklistRepository.existsByBody(tokenBody);
    }

    @Override
    public TokenBlacklist getById(Long id){
        return tokenBlacklistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Токен с таким id в чёрном списке не найден"));
    }
}
