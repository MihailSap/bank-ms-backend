package ru.sapegin.service;

import ru.sapegin.dto.ClientProductDTO;
import ru.sapegin.model.ProductRegistry;

public interface ProductRegistryServiceI {

    ProductRegistry create(ClientProductDTO clientProductDTO);
}
