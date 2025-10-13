package ru.sapegin.service;

import ru.sapegin.model.TokenBlacklist;

public interface TokenBlacklistServiceI {

    void add(String body);

    void remove(Long id);

    boolean contains(String tokenBody);

    TokenBlacklist getById(Long id);
}
