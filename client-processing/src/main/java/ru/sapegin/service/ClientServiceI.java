package ru.sapegin.service;

import ru.sapegin.dto.ClientDTO;
import ru.sapegin.dto.ClientFastDTO;
import ru.sapegin.model.Client;
import ru.sapegin.model.User;

public interface ClientServiceI {

    void create(ClientDTO clientDTO, User user);

    boolean existsInBlacklist(ClientDTO clientDTO);

    Client getClientById(Long id);

    ClientFastDTO mapToDTO(Client client);

    Client mapFromDTO(ClientDTO clientDTO, String clientId, User user);
}
