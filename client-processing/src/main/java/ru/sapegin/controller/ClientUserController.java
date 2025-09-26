package ru.sapegin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.sapegin.dto.ClientFastDTO;
import ru.sapegin.dto.RegistrationDTO;
import ru.sapegin.dto.UserDTO;
import ru.sapegin.service.ClientService;
import ru.sapegin.service.UserService;

@RestController
@RequestMapping("/api/ms1")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ClientUserController {

    private final ClientService clientService;
    private final UserService userService;

    @PostMapping("/register")
    public UserDTO register(@RequestBody RegistrationDTO registrationDTO){
        var user = userService.create(registrationDTO.userDTO());
        clientService.create(registrationDTO.clientDTO(), user);
        return userService.mapToDTO(user);
    }

    @GetMapping("/client/{id}")
    public ClientFastDTO getClientData(@PathVariable("id") Long id){
        var client = clientService.getClientById(id);
        return clientService.mapToDTO(client);
    }
}