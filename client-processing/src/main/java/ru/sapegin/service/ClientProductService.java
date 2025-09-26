package ru.sapegin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sapegin.dto.ClientProductDTO;
import ru.sapegin.dto.ClientProductFullDTO;
import ru.sapegin.enums.StatusEnum;
import ru.sapegin.model.ClientProduct;
import ru.sapegin.repository.ClientProductRepository;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientProductService {

    private final ClientProductRepository clientProductRepository;
    private final ProductService productService;
    private final ClientService clientService;

    @Transactional
    public ClientProductFullDTO create(ClientProductDTO clientProductDTO){
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

    public ClientProductFullDTO mapToFullDTO(ClientProduct clientProduct){
        return new ClientProductFullDTO(
                clientProduct.getClient().getId(),
                clientProduct.getProduct().getId(),
                clientProduct.getOpenDate(),
                clientProduct.getStatus()
        );
    }
}
