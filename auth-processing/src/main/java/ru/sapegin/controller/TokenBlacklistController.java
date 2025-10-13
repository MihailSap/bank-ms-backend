package ru.sapegin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.sapegin.dto.TokenDTO;
import ru.sapegin.service.impl.TokenBlacklistServiceImpl;

@RestController
@RequestMapping("/api/blacklist-token")
public class TokenBlacklistController {

    private final TokenBlacklistServiceImpl tokenBlacklistService;

    @Autowired
    public TokenBlacklistController(TokenBlacklistServiceImpl tokenBlacklistService) {
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @PostMapping("/add")
    public void add(@RequestBody TokenDTO tokenDTO){
        tokenBlacklistService.add(tokenDTO.body());
    }

    @DeleteMapping("/{id}/remove")
    public void remove(@PathVariable("id") Long id){
        tokenBlacklistService.remove(id);
    }
}
