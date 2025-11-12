package ru.sapegin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sapegin.dto.ClientProductDTO;
import ru.sapegin.model.ProductRegistry;
import ru.sapegin.repository.ProductRegistryRepository;
import ru.sapegin.service.impl.ProductRegistryServiceImpl;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductRegistryServiceTest {

    @Mock
    private ProductRegistryRepository productRegistryRepository;

    @Spy
    @InjectMocks
    private ProductRegistryServiceImpl productRegistryService;

    @Test
    @DisplayName("Создание ProductRegistry")
    void createTest() {
        ClientProductDTO dto = new ClientProductDTO(1L, 2L, "KEY");
        when(productRegistryRepository.save(Mockito.any(ProductRegistry.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        ProductRegistry result = productRegistryService.create(dto);
        assertNotNull(result);
        assertEquals(dto.productId(), result.getProductId());
        assertEquals(dto.clientId(), result.getClientId());
        assertEquals(0L, result.getAccountId());
        assertEquals(12, result.getInterestRate());
        assertNotNull(result.getOpenDate());
        verify(productRegistryRepository).save(result);
    }
}
