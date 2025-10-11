package ru.sapegin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sapegin.dto.UserDTO;
import ru.sapegin.model.User;
import ru.sapegin.repository.UserRepository;
import ru.sapegin.service.UserServiceI;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserServiceImpl implements UserServiceI {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public User create(UserDTO userDTO) {
        checkUnique(userDTO);
        var user = new User(userDTO.login(), passwordEncoder.encode(userDTO.password()), userDTO.email());
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
        return new UserDTO(user.getLogin(), user.getPassword(), user.getEmail());
    }

    public User getUserByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow(() -> new RuntimeException("User с таким login не найден"));
    }
}
