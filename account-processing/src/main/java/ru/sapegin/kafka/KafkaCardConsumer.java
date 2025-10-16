package ru.sapegin.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.sapegin.dto.CardDTO;
import ru.sapegin.model.Card;
import ru.sapegin.service.impl.CardServiceImpl;

@Getter
@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaCardConsumer {

    private final ObjectMapper objectMapper;
    private final CardServiceImpl cardService;

    @KafkaListener(topics = "client_cards")
    public void createCard(String message) throws JsonProcessingException {
        var cardDTO = objectMapper.readValue(message, CardDTO.class);
        Card newCard = cardService.create(cardDTO);
        CardDTO newCardDTO = cardService.mapToDTO(newCard);
        log.info("LISTENER Card: {}", newCardDTO);
    }
}
