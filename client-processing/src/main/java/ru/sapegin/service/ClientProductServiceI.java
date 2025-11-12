package ru.sapegin.service;

import ru.sapegin.dto.ClientProductDTO;
import ru.sapegin.dto.ClientProductFullDTO;
import ru.sapegin.model.ClientProduct;

public interface ClientProductServiceI {

    ClientProduct create(ClientProductDTO clientProductDTO);

    ClientProductFullDTO mapToFullDTO(ClientProduct clientProduct);
}
