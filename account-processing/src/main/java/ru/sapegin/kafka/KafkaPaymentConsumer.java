package ru.sapegin.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.sapegin.dto.PaymentDTO;
import ru.sapegin.service.impl.PaymentServiceImpl;

@Getter
@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaPaymentConsumer {

    private final ObjectMapper objectMapper;
    private final PaymentServiceImpl paymentService;

    @KafkaListener(topics = "client_payments")
    public void listenClientProducts(String message) throws JsonProcessingException {
        var paymentDTO = objectMapper.readValue(message, PaymentDTO.class);
        paymentService.closeCredit(paymentDTO);
        log.info("LISTENER: Payment: {}", message);
    }
}
