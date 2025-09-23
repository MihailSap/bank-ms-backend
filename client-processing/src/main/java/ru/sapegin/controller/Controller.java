package ru.sapegin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sapegin.dto.RegistrationDTO;
import ru.sapegin.service.ClientService;
import ru.sapegin.service.UserService;

@RestController
@RequestMapping("/api/ms1")
public class Controller {

    private final ClientService clientService;
    private final UserService userService;

    @Autowired
    public Controller(ClientService clientService, UserService userService) {
        this.clientService = clientService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public String register(@RequestBody RegistrationDTO registrationDTO){
        var user = userService.createAndGet(registrationDTO.userDTO());
        clientService.create(registrationDTO.clientDTO(), user);
        return "Success";
    }
}