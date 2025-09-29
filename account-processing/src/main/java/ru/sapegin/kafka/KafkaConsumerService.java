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
import ru.sapegin.dto.PaymentDTO;
import ru.sapegin.dto.TransactionDTO;
import ru.sapegin.service.impl.AccountServiceImpl;
import ru.sapegin.service.impl.CardServiceImpl;
import ru.sapegin.service.impl.PaymentServiceImpl;
import ru.sapegin.service.impl.TransactionServiceImpl;

//@Getter
//@Slf4j
//@Service
//@RequiredArgsConstructor
public class KafkaConsumerService {

//    private final ObjectMapper objectMapper;
////    private final AccountServiceImpl accountService;
////    private final CardServiceImpl cardService;
////    private final TransactionServiceImpl transactionService;
//    private final PaymentServiceImpl paymentService;

//    @KafkaListener(topics = "client_products")
//    public void createAccount(String message) throws JsonProcessingException {
//        var clientProductDTO = objectMapper.readValue(message, ClientProductDTO.class);
//        var accountDTO = accountService.create(clientProductDTO);
//        log.info("LISTENER Account: {}", accountDTO);
//    }

//    @KafkaListener(topics = "client_cards")
//    public void createCard(String message) throws JsonProcessingException {
//        var cardDTO = objectMapper.readValue(message, CardDTO.class);
//        var newCardDTO = cardService.create(cardDTO);
//        log.info("LISTENER Card: {}", newCardDTO);
//    }

//    @KafkaListener(topics = "client_transactions")
//    public void listenClientTransactions(String message) throws JsonProcessingException {
//        var transactionDTO = objectMapper.readValue(message, TransactionDTO.class);
//        var transaction = transactionService.proccessGetTransaction(transactionDTO);
//        log.info("LISTENER: Transaction: {}", transaction);
//    }

//    @KafkaListener(topics = "client_payments")
//    public void listenClientProducts(String message) throws JsonProcessingException {
//        var paymentDTO = objectMapper.readValue(message, PaymentDTO.class);
//        paymentService.closeCredit(paymentDTO);
//        log.info("LISTENER: Payment: {}", message);
//    }
}