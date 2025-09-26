package ru.sapegin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sapegin.dto.UserDTO;
import ru.sapegin.model.User;
import ru.sapegin.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User create(UserDTO userDTO){
        checkUnique(userDTO);
        var user = new User(userDTO.login(), userDTO.password(), userDTO.email());
        userRepository.save(user);
        log.info("СОЗДАН User: {}", user);
        return user;
    }

    public void checkUnique(UserDTO userDTO){
        if(!isUniqueLogin(userDTO)){
            throw new RuntimeException("Пользователь с таким login существует!");
        }
        if(!isUniqueEmail(userDTO)){
            throw new RuntimeException("Пользователь с таким email существует!");
        }
    }

    public boolean isUniqueLogin(UserDTO userDTO){
        var login = userDTO.login();
        var optUser = userRepository.findByLogin(login);
        return optUser.isEmpty();
    }

    public boolean isUniqueEmail(UserDTO userDTO){
        var email = userDTO.email();
        var optUser = userRepository.findByEmail(email);
        return optUser.isEmpty();
    }

    public UserDTO mapToDTO(User user){
        return new UserDTO(user.getLogin(), user.getPassword(), user.getEmail());
    }
}
