package ru.sapegin.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.sapegin.dto.ClientProductDTO;
import ru.sapegin.service.impl.AccountServiceImpl;

@Getter
@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaAccountConsumer {

    private final ObjectMapper objectMapper;
    private final AccountServiceImpl accountService;

    @KafkaListener(topics = "client_products")
    public void createAccount(String message) throws JsonProcessingException {
        var clientProductDTO = objectMapper.readValue(message, ClientProductDTO.class);
        var accountDTO = accountService.create(clientProductDTO);
        log.info("LISTENER Account: {}", accountDTO);
    }
}
