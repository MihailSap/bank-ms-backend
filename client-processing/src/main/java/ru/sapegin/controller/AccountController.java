package ru.sapegin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sapegin.dto.ClientProductDTO;
import ru.sapegin.dto.ClientProductKeyDTO;
import ru.sapegin.kafka.KafkaProducerService;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final KafkaProducerService kafkaProducerService;

    @PostMapping("/create")
    public void createClientProduct(@RequestBody ClientProductKeyDTO clientProductDTO) {
        kafkaProducerService.inspectClientProduct(clientProductDTO);
    }
}
