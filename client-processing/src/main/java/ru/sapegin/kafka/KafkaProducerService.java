package ru.sapegin.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.sapegin.dto.CardDTO;
import ru.sapegin.dto.ClientProductDTO;
import ru.sapegin.enums.KeyEnum;
import ru.sapegin.service.ProductService;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    private final ProductService productService;

    public void inspectClientProduct(ClientProductDTO clientProductDTO){
        var product = productService.getProductById(clientProductDTO.productId());
        var productKey = product.getKey();
        if(productKey == KeyEnum.DC || productKey == KeyEnum.CC || productKey == KeyEnum.NS || productKey == KeyEnum.PENS){
            kafkaTemplate.send("client_products", clientProductDTO);
        } else {
            kafkaTemplate.send("client_credit_products", clientProductDTO);
        }
    }

    public void createCard(CardDTO cardDTO) {
        kafkaTemplate.send("client_cards", cardDTO);
    }
}
