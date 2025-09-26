package ru.sapegin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.sapegin.dto.ClientProductDTO;
import ru.sapegin.dto.ClientProductFullDTO;
import ru.sapegin.kafka.KafkaProducerService;
import ru.sapegin.service.impl.ClientProductServiceImpl;

@RestController
@RequestMapping("/api/client-product")
@RequiredArgsConstructor
public class ClientProductController {

    private final KafkaProducerService kafkaProducerService;
    private final ClientProductServiceImpl clientProductService;

    @PostMapping("/create")
    public void createClientProduct(@RequestBody ClientProductDTO clientProductDTO) {
        kafkaProducerService.inspectClientProduct(clientProductDTO);
    }

    @PostMapping("/create-credit")
    public ClientProductFullDTO createClientCredit(@RequestBody ClientProductDTO clientProductDTO){
        return clientProductService.create(clientProductDTO);
    }
}
