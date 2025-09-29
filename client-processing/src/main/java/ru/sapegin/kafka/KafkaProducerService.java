package ru.sapegin.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.sapegin.dto.*;
import ru.sapegin.enums.KeyEnum;
import ru.sapegin.service.impl.ProductServiceImpl;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    private final ProductServiceImpl productService;

    public void inspectClientProduct(ClientProductKeyDTO clientProductDTO){
        var product = productService.getProductById(clientProductDTO.getProductId());
        var productKey = product.getKey();
        clientProductDTO.setKeyType(productKey);
        if(productKey == KeyEnum.DC || productKey == KeyEnum.CC || productKey == KeyEnum.NS || productKey == KeyEnum.PENS){
            kafkaTemplate.send("client_products", clientProductDTO);
        } else {
            kafkaTemplate.send("client_credit_products", clientProductDTO);
        }
    }

    public void createCard(CardDTO cardDTO) {
        kafkaTemplate.send("client_cards", cardDTO);
    }

    public void createTransaction(TransactionDTO transactionDTO){
        kafkaTemplate.send("client_transactions", UUID.randomUUID().toString(), transactionDTO);
    }

    public void createPayment(PaymentDTO paymentDTO){
        kafkaTemplate.send("client_payments", UUID.randomUUID().toString(), paymentDTO);
    }
}
