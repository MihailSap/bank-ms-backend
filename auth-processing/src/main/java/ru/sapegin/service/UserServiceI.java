package ru.sapegin.service;

import ru.sapegin.dto.UserDTO;
import ru.sapegin.model.User;

public interface UserServiceI {

    User create(UserDTO userDTO);

    void checkUnique(UserDTO userDTO);

    boolean isUniqueLogin(UserDTO userDTO);

    boolean isUniqueEmail(UserDTO userDTO);

    UserDTO mapToDTO(User user);
}
