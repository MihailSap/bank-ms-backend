package ru.sapegin.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.sapegin.dto.ClientDataDTO;
import ru.sapegin.dto.ClientProductDTO;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaConsumer {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final int N = 1000000;

    @KafkaListener(topics = "client_credit_products")
    public void listen(String message) throws JsonProcessingException {
        var clientProductDTO = objectMapper.readValue(message, ClientProductDTO.class);
        var clientDataDTO = getClientInfo(clientProductDTO.clientId());

    }

    public ClientDataDTO getClientInfo(Long clientId) {
        log.info("Getting client info");
        var url = "http://localhost:8082/api/ms1/client/" + clientId;
        return restTemplate.getForObject(url, ClientDataDTO.class);
    }
}
