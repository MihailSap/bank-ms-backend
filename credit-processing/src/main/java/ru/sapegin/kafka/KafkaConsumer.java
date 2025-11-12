package ru.sapegin.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.sapegin.dto.ClientProductDTO;
import ru.sapegin.service.impl.CreditServiceImpl;
import ru.sapegin.service.impl.ProductRegistryServiceImpl;
import ru.sapegin.service.impl.RestServiceImpl;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final ObjectMapper objectMapper;
    private final RestServiceImpl restService;
    private final CreditServiceImpl creditService;
    private final ProductRegistryServiceImpl productRegistryService;

    @KafkaListener(topics = "client_credit_products")
    public void listen(String message) throws JsonProcessingException {
        var clientProductDTO = objectMapper.readValue(message, ClientProductDTO.class);
        var clientDataDTO = restService.getClientInfo(clientProductDTO.clientId());
        log.info(clientDataDTO.toString());
        if (creditService.canClientOpenCredit(clientProductDTO.clientId())){
            restService.createClientProduct(clientProductDTO);
            productRegistryService.create(clientProductDTO);
        } else {
            throw new RuntimeException("Отказано");
        }
    }
}
