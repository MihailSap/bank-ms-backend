package ru.sapegin.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.sapegin.dto.ClientProductDTO;
import ru.sapegin.dto.ClientProductFullDTO;
import ru.sapegin.kafka.KafkaProducerService;
import ru.sapegin.service.ClientProductService;

@RestController
@RequestMapping("/api/client-product")
@RequiredArgsConstructor
public class ClientProductController {

    private static final Logger log = LoggerFactory.getLogger(ClientProductController.class);
    private final KafkaProducerService kafkaProducerService;
    private final ClientProductService clientProductService;

    @PostMapping("/create")
    public void createClientProduct(@RequestBody ClientProductDTO clientProductDTO) {
        kafkaProducerService.inspectClientProduct(clientProductDTO);
    }

    @PostMapping("/create-credit")
    public ClientProductFullDTO createClientCredit(@RequestBody ClientProductDTO clientProductDTO){
        return clientProductService.create(clientProductDTO);
    }
}
