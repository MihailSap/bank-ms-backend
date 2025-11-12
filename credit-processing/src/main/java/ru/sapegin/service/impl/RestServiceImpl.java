package ru.sapegin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.sapegin.dto.ClientDataDTO;
import ru.sapegin.dto.ClientProductDTO;
import ru.sapegin.service.RestServiceI;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestServiceImpl implements RestServiceI {

    private final RestTemplate restTemplate;

    @Override
    public ClientDataDTO getClientInfo(Long clientId) {
        var url = "http://localhost:8082/api/ms1/client/" + clientId;
        var clientInfo = restTemplate.getForObject(url, ClientDataDTO.class);
        log.info("ПОЛУЧЕНА информация о клиенте: {}", clientInfo);
        return clientInfo;
    }

    @Override
    public void createClientProduct(ClientProductDTO clientProductDTO) {
        var url = "http://localhost:8082/api/client-product/create-credit";
        var clientProduct = restTemplate.postForObject(url, clientProductDTO, ClientProductDTO.class);
        log.info("ПОЛУЧЕН созданный ClientProductDTO: {}", clientProduct);
    }
}
