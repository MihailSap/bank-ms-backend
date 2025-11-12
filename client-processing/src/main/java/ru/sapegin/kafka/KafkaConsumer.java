package ru.sapegin.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import ru.sapegin.dto.ClientProductDTO;

public class KafkaConsumer {

    @KafkaListener(topics = "client-product-decision")
    public void createOrGetException(boolean isGood, ClientProductDTO clientProductDTO){
        if(!isGood){
            throw new RuntimeException("Кредит не одобрен");
        }

    }
}
