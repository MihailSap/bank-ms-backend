package ru.sapegin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.sapegin.dto.ClientProductDTO;
import ru.sapegin.dto.ClientProductFullDTO;
import ru.sapegin.model.ClientProduct;
import ru.sapegin.service.impl.ClientProductServiceImpl;

@RestController
@RequestMapping("/api/client-product")
@RequiredArgsConstructor
public class ClientProductController {

    private final ClientProductServiceImpl clientProductService;

    @PostMapping("/create-credit")
    public ClientProductFullDTO createClientCredit(@RequestBody ClientProductDTO clientProductDTO){
        ClientProduct clientProduct = clientProductService.create(clientProductDTO);
        return clientProductService.mapToFullDTO(clientProduct);
    }
}
