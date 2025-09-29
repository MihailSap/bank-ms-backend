package ru.sapegin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sapegin.dto.TransactionDTO;
import ru.sapegin.kafka.KafkaProducerService;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final KafkaProducerService kafkaProducerService;

    @PostMapping("/create")
    public void create(@RequestBody TransactionDTO transactionDTO){
        kafkaProducerService.createTransaction(transactionDTO);
    }
}
