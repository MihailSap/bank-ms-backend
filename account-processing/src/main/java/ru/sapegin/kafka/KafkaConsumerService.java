package ru.sapegin.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.sapegin.dto.ClientProductDTO;
import ru.sapegin.dto.CardDTO;
import ru.sapegin.service.AccountService;
import ru.sapegin.service.CardService;

@Getter
@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final AccountService accountService;
    private final CardService cardService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "client_products")
    public void createAccount(String message) throws JsonProcessingException {
        var clientProductDTO = objectMapper.readValue(message, ClientProductDTO.class);
        var accountDTO = accountService.create(clientProductDTO);
        log.info("Created account: {}", accountDTO);
    }

    @KafkaListener(topics = "client_cards")
    public void createCard(String message) throws JsonProcessingException {
        var cardDTO = objectMapper.readValue(message, CardDTO.class);
        var newCardDTO = cardService.create(cardDTO);
        log.info("Создана карта: {}", newCardDTO);
    }

    @KafkaListener(topics = "client_transactions")
    public void listenClientTransactions(){
        log.info("Ещё один топик");
    }
}