package ru.sapegin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.sapegin.dto.ClientProductDTO;

import java.util.ArrayList;
import java.util.List;

@Getter
@Slf4j
@Service
@RequiredArgsConstructor
public class MyConsumerService {

    private final AccountService accountService;
    private final List<String> myMessages = new ArrayList<>();
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "client_cards")
    public void saveMessage(String message) {
        myMessages.add(message);
        log.info("Сообщение получено и сохранено: {}", message);
    }

    @KafkaListener(topics = "client_products")
    public void createAccount(String message) throws JsonProcessingException {
        log.info("Сообщение из Kafka: {}", message);
        var clientProductDTO = objectMapper.readValue(message, ClientProductDTO.class);
        accountService.create(clientProductDTO);
        log.info("Создан аккаунт");
    }
}