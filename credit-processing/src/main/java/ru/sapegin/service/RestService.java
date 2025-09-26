package ru.sapegin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.sapegin.dto.ClientDataDTO;
import ru.sapegin.dto.ClientProductDTO;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestService {
    private final RestTemplate restTemplate;

    public ClientDataDTO getClientInfo(Long clientId) {
        var url = "http://localhost:8082/api/ms1/client/" + clientId;
        var clientInfo = restTemplate.getForObject(url, ClientDataDTO.class);
        log.info("ПОЛУЧЕНА информация о клиенте: {}", clientInfo);
        return clientInfo;
    }

    public void createClientProduct(ClientProductDTO clientProductDTO){
        var url = "http://localhost:8082/api/client-product/create-credit";
        var clientProduct = restTemplate.postForObject(url, clientProductDTO, ClientProductDTO.class);
        log.info("ПОЛУЧЕН созданный ClientProductDTO: {}", clientProduct);
    }
}
