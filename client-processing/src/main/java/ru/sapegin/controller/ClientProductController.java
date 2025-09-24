package ru.sapegin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sapegin.dto.ClientProductDTO;
import ru.sapegin.service.ClientProductService;

@RestController
@RequestMapping("/api/client-product")
@RequiredArgsConstructor
public class ClientProductController {

    private final ClientProductService clientProductService;

    @PostMapping("/create")
    public void createClientProduct(@RequestBody ClientProductDTO clientProductDTO) {
        clientProductService.inspectClientProduct(clientProductDTO);
    }
}
