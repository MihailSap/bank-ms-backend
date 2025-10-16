package ru.sapegin.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sapegin.dto.ClientDTO;
import ru.sapegin.dto.ClientFastDTO;
import ru.sapegin.enums.DocumentTypeEnum;
import ru.sapegin.model.BlacklistRegistry;
import ru.sapegin.model.Client;
import ru.sapegin.repository.BlacklistRegistryRepository;
import ru.sapegin.repository.ClientRepository;
import ru.sapegin.service.impl.ClientServiceImpl;

import java.time.LocalDate;
import java.util.Optional;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private BlacklistRegistryRepository blacklistRegistryRepository;

    @Spy
    @InjectMocks
    private ClientServiceImpl clientService;

    private final ClientDTO dto = new ClientDTO(
            "Иван", "Иванович", "Иванов",
            LocalDate.of(1990, 1, 1),
            DocumentTypeEnum.PASSPORT,
            123456L,
            "AB", "789012",
            12, 34
    );

    @Test
    @DisplayName("Клиент не находится в blacklist")
    void notInBlacklistTest() {
        when(blacklistRegistryRepository.findByDocumentId(dto.documentId()))
                .thenReturn(Optional.empty());
        boolean result = clientService.existsInBlacklist(dto);
        assertFalse(result);
    }

    @Test
    @DisplayName("Клиент в blacklist, срок не истек")
    void inBlacklistNotExpiredTest() {
        var blacklistEntry = new BlacklistRegistry();
        blacklistEntry.setBlacklistExpirationDate(LocalDate.now().plusDays(1));
        when(blacklistRegistryRepository.findByDocumentId(dto.documentId()))
                .thenReturn(Optional.of(blacklistEntry));
        boolean result = clientService.existsInBlacklist(dto);
        assertTrue(result);
    }

    @Test
    @DisplayName("Клиент в blacklist, срок истёк")
    void inBlacklistExpiredTest() {
        var blacklistEntry = new BlacklistRegistry();
        blacklistEntry.setBlacklistExpirationDate(LocalDate.now().minusDays(1));
        when(blacklistRegistryRepository.findByDocumentId(dto.documentId()))
                .thenReturn(Optional.of(blacklistEntry));
        boolean result = clientService.existsInBlacklist(dto);
        assertFalse(result);
    }

    @Test
    @DisplayName("Маппинг ClientDTO в Client")
    void mapFromDTOTest() {
        String expectedClientId = "123400000016";
        Long expectedUserId = 5L;
        Client expected = new Client(
                expectedClientId,
                expectedUserId,
                dto.firstName(),
                dto.middleName(),
                dto.lastName(),
                dto.dateOfBirth(),
                dto.documentType(),
                dto.documentId(),
                dto.documentPrefix(),
                dto.documentSuffix()
        );

        Client actual = clientService.mapFromDTO(dto, expectedClientId, expectedUserId);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Маппинг Client в ClientFastDTO")
    void mapToDTOTest() {
        Client client = new Client();
        client.setFirstName("Петр");
        client.setMiddleName("Петрович");
        client.setLastName("Петров");
        client.setDocumentId(654321L);

        ClientFastDTO expected = new ClientFastDTO();
        expected.setFirstName("Петр");
        expected.setMiddleName("Петрович");
        expected.setLastName("Петров");
        expected.setDocumentId(654321L);

        ClientFastDTO actual = clientService.mapToDTO(client);
        assertEquals(expected, actual);
    }
}
