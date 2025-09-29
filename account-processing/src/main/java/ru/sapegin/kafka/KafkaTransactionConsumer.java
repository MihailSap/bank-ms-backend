package ru.sapegin.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.sapegin.dto.TransactionDTO;
import ru.sapegin.service.impl.TransactionServiceImpl;

@Getter
@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaTransactionConsumer {

    private final ObjectMapper objectMapper;
    private final TransactionServiceImpl transactionService;

    @KafkaListener(topics = "client_transactions")
    public void listenClientTransactions(
            @Payload String message,
            @Header(KafkaHeaders.RECEIVED_KEY) String key
    ) throws JsonProcessingException {
        var transactionDTO = objectMapper.readValue(message, TransactionDTO.class);
        var transaction = transactionService.proccessGetTransaction(transactionDTO);
        log.info("LISTENER: Transaction: {}; Key: {}", transaction, key);
    }
}
