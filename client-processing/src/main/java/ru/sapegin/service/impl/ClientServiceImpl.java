package ru.sapegin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sapegin.dto.ClientDTO;
import ru.sapegin.dto.ClientFastDTO;
import ru.sapegin.model.Client;
import ru.sapegin.repository.BlacklistRegistryRepository;
import ru.sapegin.repository.ClientRepository;
import ru.sapegin.service.ClientServiceI;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ClientServiceImpl implements ClientServiceI {

    private final BlacklistRegistryRepository blacklistRegistryRepository;
    private final ClientRepository clientRepository;

    @Transactional
    @Override
    public Client create(ClientDTO clientDTO, Long userId) {
        if(existsInBlacklist(clientDTO)){
            throw new RuntimeException("Клиент находится в чёрном списке");
        }

        var prefix = String.format("%02d", clientDTO.regionNumber()) + String.format("%02d", clientDTO.bankDivisionNumber());
        var clientId = String.format("%02d%02d%08d",
                clientDTO.regionNumber(), clientDTO.bankDivisionNumber(),
                clientRepository.countByRegionAndDivision(prefix) + 1);

        var client = mapFromDTO(clientDTO, clientId, userId);
        clientRepository.save(client);
        log.info("СОЗДАН Client: {}", client);
        return client;
    }

    @Override
    public boolean existsInBlacklist(ClientDTO clientDTO) {
        var data = blacklistRegistryRepository.findByDocumentId(clientDTO.documentId());
        if (data.isEmpty()) {
            return false;
        }
        var now = LocalDate.now();
        var blackExpirationDate = data.get().getBlacklistExpirationDate();
        return blackExpirationDate == null || now.isBefore(blackExpirationDate);
    }

    @Override
    public Client getClientById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Клиент с id %d не найден", id)));
    }

    @Override
    public ClientFastDTO mapToDTO(Client client) {
        var clientFastDTO = new ClientFastDTO();
        clientFastDTO.setFirstName(client.getFirstName());
        clientFastDTO.setMiddleName(client.getMiddleName());
        clientFastDTO.setLastName(client.getLastName());
        clientFastDTO.setDocumentId(client.getDocumentId());
        return clientFastDTO;
    }

    @Override
    public Client mapFromDTO(ClientDTO clientDTO, String clientId, Long userId) {
        return new Client(
                clientId,
                userId,
                clientDTO.firstName(),
                clientDTO.middleName(),
                clientDTO.lastName(),
                clientDTO.dateOfBirth(),
                clientDTO.documentType(),
                clientDTO.documentId(),
                clientDTO.documentPrefix(),
                clientDTO.documentSuffix()
        );
    }
}
