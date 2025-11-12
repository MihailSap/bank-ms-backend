package ru.sapegin.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sapegin.dto.UserDTO;
import ru.sapegin.enums.Role;
import ru.sapegin.model.RefreshToken;
import ru.sapegin.model.User;
import ru.sapegin.repository.UserRepository;
import ru.sapegin.service.UserServiceI;

@Slf4j
@Service
public class UserServiceImpl implements UserServiceI {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public User create(UserDTO userDTO) {
        checkUnique(userDTO);
        var user = new User(userDTO.login(), passwordEncoder.encode(userDTO.password()), userDTO.email());
        user.setRole(Role.CURRENT_CLIENT);
        userRepository.save(user);
        log.info("СОЗДАН User: {}", user);
        return user;
    }

    @Override
    public void checkUnique(UserDTO userDTO) {
        if(!isUniqueLogin(userDTO)){
            throw new RuntimeException("Пользователь с таким login существует!");
        }
        if(!isUniqueEmail(userDTO)){
            throw new RuntimeException("Пользователь с таким email существует!");
        }
    }

    @Override
    public boolean isUniqueLogin(UserDTO userDTO) {
        var login = userDTO.login();
        var optUser = userRepository.findByLogin(login);
        return optUser.isEmpty();
    }

    @Override
    public boolean isUniqueEmail(UserDTO userDTO) {
        var email = userDTO.email();
        var optUser = userRepository.findByEmail(email);
        return optUser.isEmpty();
    }

    @Override
    public UserDTO mapToDTO(User user) {
        return new UserDTO(user.getId(), user.getLogin(), null, user.getEmail());
    }

    public User getUserByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow(() -> new RuntimeException("User с таким login не найден"));
    }

    @Transactional
    public void updateRefreshToken(User user, String refreshTokenBody){
        if (user.getRefreshToken() != null) {
            user.getRefreshToken().setBody(refreshTokenBody);
        } else {
            user.setRefreshToken(new RefreshToken(refreshTokenBody, user));
        }
        userRepository.save(user);
    }
}
