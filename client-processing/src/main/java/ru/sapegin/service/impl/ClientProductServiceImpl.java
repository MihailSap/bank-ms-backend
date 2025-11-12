package ru.sapegin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sapegin.dto.ClientProductDTO;
import ru.sapegin.dto.ClientProductFullDTO;
import ru.sapegin.enums.StatusEnum;
import ru.sapegin.model.ClientProduct;
import ru.sapegin.repository.ClientProductRepository;
import ru.sapegin.service.ClientProductServiceI;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientProductServiceImpl implements ClientProductServiceI {

    private final ClientProductRepository clientProductRepository;
    private final ProductServiceImpl productService;
    private final ClientServiceImpl clientService;

    @Transactional
    @Override
    public ClientProductFullDTO create(ClientProductDTO clientProductDTO) {
        var client = clientService.getClientById(clientProductDTO.clientId());
        var product = productService.getProductById(clientProductDTO.productId());
        var clientProduct = new ClientProduct();
        clientProduct.setClient(client);
        clientProduct.setProduct(product);
        clientProduct.setOpenDate(LocalDate.now());
        clientProduct.setStatus(StatusEnum.ACTIVE);
        clientProductRepository.save(clientProduct);
        log.info("СОЗДАН ClientProduct: {}", clientProduct);
        return mapToFullDTO(clientProduct);
    }

    @Override
    public ClientProductFullDTO mapToFullDTO(ClientProduct clientProduct) {
        return new ClientProductFullDTO(
                clientProduct.getClient().getId(),
                clientProduct.getProduct().getId(),
                clientProduct.getOpenDate(),
                clientProduct.getStatus()
        );
    }
}
