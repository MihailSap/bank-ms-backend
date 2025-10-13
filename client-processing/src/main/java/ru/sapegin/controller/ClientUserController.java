package ru.sapegin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.sapegin.aspect.annotation.Cached;
import ru.sapegin.dto.ClientFastDTO;
import ru.sapegin.dto.RegistrationDTO;
import ru.sapegin.dto.UserDTO;
import ru.sapegin.service.impl.ClientServiceImpl;

@RestController
@RequestMapping("/api/ms1")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ClientUserController {

    private final ClientServiceImpl clientService;
    private final RestTemplate restTemplate;


    @PostMapping("/register")
    public UserDTO register(@RequestBody RegistrationDTO registrationDTO){
        UserDTO userDTO = restTemplate.postForObject("http://localhost:8084/api/auth/register", registrationDTO.userDTO(), UserDTO.class);
        clientService.create(registrationDTO.clientDTO(), userDTO.id());
        return userDTO;
    }

    @Cached(cacheByPrimaryKey = true)
    @GetMapping("/client/{id}")
    public ClientFastDTO getClientData(@PathVariable("id") Long id){
        var client = clientService.getClientById(id);
        return clientService.mapToDTO(client);
    }
}