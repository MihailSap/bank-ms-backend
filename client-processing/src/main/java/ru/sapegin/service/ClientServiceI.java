package ru.sapegin.service;

import ru.sapegin.dto.ClientDTO;
import ru.sapegin.dto.ClientFastDTO;
import ru.sapegin.model.Client;

public interface ClientServiceI {

    Client create(ClientDTO clientDTO, Long userId);

    boolean existsInBlacklist(ClientDTO clientDTO);

    Client getClientById(Long id);

    ClientFastDTO mapToDTO(Client client);

    Client mapFromDTO(ClientDTO clientDTO, String clientId, Long userId);
}
