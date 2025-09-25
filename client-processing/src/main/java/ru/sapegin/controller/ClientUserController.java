package ru.sapegin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.sapegin.dto.ClientDTO;
import ru.sapegin.dto.ClientFastDTO;
import ru.sapegin.dto.RegistrationDTO;
import ru.sapegin.service.ClientService;
import ru.sapegin.service.UserService;

@RestController
@RequestMapping("/api/ms1")
public class ClientUserController {

    private final ClientService clientService;
    private final UserService userService;

    @Autowired
    public ClientUserController(ClientService clientService, UserService userService) {
        this.clientService = clientService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public String register(@RequestBody RegistrationDTO registrationDTO){
        var user = userService.createAndGet(registrationDTO.userDTO());
        clientService.create(registrationDTO.clientDTO(), user);
        return "Success";
    }

    @GetMapping("/client/{id}")
    public ClientFastDTO getClientData(@PathVariable("id") Long id){
        return clientService.getClientDTO(id);
    }
}