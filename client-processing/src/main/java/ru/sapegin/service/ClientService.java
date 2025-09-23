package ru.sapegin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sapegin.dto.ClientDTO;
import ru.sapegin.model.Client;
import ru.sapegin.model.User;
import ru.sapegin.repository.BlacklistRegistryRepository;
import ru.sapegin.repository.ClientRepository;

import java.time.LocalDate;

@Service
public class ClientService {

    private final BlacklistRegistryRepository blacklistRegistryRepository;

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(BlacklistRegistryRepository blacklistRegistryRepository, ClientRepository clientRepository) {
        this.blacklistRegistryRepository = blacklistRegistryRepository;
        this.clientRepository = clientRepository;
    }

    @Transactional
    public void create(ClientDTO clientDTO, User user){
        var data = blacklistRegistryRepository.findByDocumentId(clientDTO.documentId());
        if (data.isPresent()) {
            var now = LocalDate.now();
            var blackExpirationDate = data.get().getBlacklistExpirationDate();
            if(blackExpirationDate == null || now.isBefore(blackExpirationDate)) {
                throw new RuntimeException("Документ находится в чёрном списке");
            }
        }

        var sss = String.format("%02d", clientDTO.regionNumber()) + String.format("%02d", clientDTO.bankDivisionNumber());
        var clientId = String.format("%02d%02d%08d",
                clientDTO.regionNumber(), clientDTO.bankDivisionNumber(),
                clientRepository.countByRegionAndDivision(sss) + 1);

        var client = new Client(
                clientId,
                user,
                clientDTO.firstName(),
                clientDTO.middleName(),
                clientDTO.lastName(),
                clientDTO.dateOfBirth(),
                clientDTO.documentType(),
                clientDTO.documentId(),
                clientDTO.documentPrefix(),
                clientDTO.documentSuffix()
        );
        clientRepository.save(client);
    }
}
