package ru.sapegin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sapegin.dto.ClientProductDTO;
import ru.sapegin.model.ProductRegistry;
import ru.sapegin.repository.ProductRegistryRepository;
import ru.sapegin.service.ProductRegistryServiceI;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductRegistryServiceImpl implements ProductRegistryServiceI {

    private final ProductRegistryRepository productRegistryRepository;

    @Transactional
    @Override
    public ProductRegistry create(ClientProductDTO clientProductDTO) {
        var productRegistry = new ProductRegistry();
        productRegistry.setProductId(clientProductDTO.productId());
        productRegistry.setAccountId(0L);
        productRegistry.setClientId(clientProductDTO.clientId());
        productRegistry.setInterestRate(12);
        productRegistry.setOpenDate(LocalDate.now());
        productRegistryRepository.save(productRegistry);
        log.info("СОЗДАН ProductRegistry: {}", productRegistry);
        return productRegistry;
    }
}
