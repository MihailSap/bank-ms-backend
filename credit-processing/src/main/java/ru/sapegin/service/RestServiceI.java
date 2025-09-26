package ru.sapegin.service;

import ru.sapegin.dto.ClientDataDTO;
import ru.sapegin.dto.ClientProductDTO;

public interface RestServiceI {

    ClientDataDTO getClientInfo(Long clientId);

    void createClientProduct(ClientProductDTO clientProductDTO);
}
