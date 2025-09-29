package ru.sapegin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sapegin.dto.PaymentDTO;
import ru.sapegin.kafka.KafkaProducerService;

@Slf4j
@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final KafkaProducerService kafkaProducerService;

    @PostMapping("/create")
    public void create(@RequestBody PaymentDTO paymentDTO){
        kafkaProducerService.createPayment(paymentDTO);
    }
}
