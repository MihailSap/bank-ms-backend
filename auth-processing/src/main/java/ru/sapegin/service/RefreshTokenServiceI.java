package ru.sapegin.service;

import ru.sapegin.model.RefreshToken;
import ru.sapegin.model.User;

public interface RefreshTokenServiceI {

    RefreshToken getByUser(User user);

    RefreshToken getByBody(String body);

    void save(RefreshToken existingToken);
}
