package ru.sapegin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.sapegin.dto.ClientProductDTO;
import ru.sapegin.enums.KeyEnum;
import ru.sapegin.repository.ClientProductRepository;

@Service
@RequiredArgsConstructor
public class ClientProductService {

    private final ProductService productService;
    private final ClientProductRepository clientProductRepository;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void inspectClientProduct(ClientProductDTO clientProductDTO){
        var product = productService.getProductById(clientProductDTO.productId());
        var productKey = product.getKey();
        if(productKey == KeyEnum.DC || productKey == KeyEnum.CC || productKey == KeyEnum.NS || productKey == KeyEnum.PENS){
            kafkaTemplate.send("client_products", clientProductDTO);
        } else {
            kafkaTemplate.send("client_credit_products", clientProductDTO);
        }
    }
}
