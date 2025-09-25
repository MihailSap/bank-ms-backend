package ru.sapegin.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.sapegin.dto.ClientProductDTO;
import ru.sapegin.kafka.KafkaProducerService;

@RestController
@RequestMapping("/api/client-product")
@RequiredArgsConstructor
public class ClientProductController {

    private static final Logger log = LoggerFactory.getLogger(ClientProductController.class);
    private final KafkaProducerService kafkaProducerService;

    @PostMapping("/create")
    public void createClientProduct(@RequestBody ClientProductDTO clientProductDTO) {
        kafkaProducerService.inspectClientProduct(clientProductDTO);
    }

//    @GetMapping("/{id}/credits")
//    public int getCreditsAmount(@PathVariable("id") int clientId){
//
//
//    }
}
