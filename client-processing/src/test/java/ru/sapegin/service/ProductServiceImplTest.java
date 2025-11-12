package ru.sapegin.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sapegin.dto.ProductDTO;
import ru.sapegin.enums.KeyEnum;
import ru.sapegin.model.Product;
import ru.sapegin.repository.ProductRepository;
import ru.sapegin.service.impl.ProductServiceImpl;

import java.time.LocalDate;

import static junit.framework.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Spy
    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    @DisplayName("Создание Product")
    void createTest(){
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Дебетовая карта");
        productDTO.setKey(KeyEnum.DC);
        Product expected = new Product(
                "Дебетовая карта",
                KeyEnum.DC,
                LocalDate.now()
        );

        when(productRepository.save(Mockito.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));
        Product result = productService.create(productDTO);
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Изменение Product")
    void updateProduct() {
        Long productId = 1L;
        Product existingProduct = new Product("Old Name", KeyEnum.BS, LocalDate.now());
        existingProduct.setId(productId);

        ProductDTO dto = new ProductDTO();
        dto.setName("New Product");
        dto.setKey(KeyEnum.DC);

        when(productRepository.findById(productId)).thenReturn(java.util.Optional.of(existingProduct));
        when(productRepository.save(Mockito.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Product result = productService.update(productId, dto);
        assertNotNull(result);
        assertEquals("New Product", result.getName());
        assertEquals(KeyEnum.DC, result.getKey());
        assertEquals("DC1", result.getProductId());
        verify(productRepository).save(result);
    }

    @Test
    @DisplayName("Удаление Product")
    void deleteProduct(){
        Long productId = 1L;
        Product existingProduct = new Product("Кредитная карта", KeyEnum.CC, LocalDate.now());
        existingProduct.setId(productId);
        when(productRepository.findById(productId)).thenReturn(java.util.Optional.of(existingProduct));
        doNothing().when(productRepository).delete(existingProduct);
        productService.delete(productId);
        verify(productRepository).findById(productId);
        verify(productRepository).delete(existingProduct);
    }

    @Test
    @DisplayName("Маппинг Product в ProductDTO")
    void mapToDTOTest(){
        Product product = new Product("Кредитная карта", KeyEnum.CC, LocalDate.of(2024, 5, 1));
        product.setProductId("CC123");
        ProductDTO result = productService.mapToDTO(product);
        ProductDTO expected = new ProductDTO("Кредитная карта", KeyEnum.CC, LocalDate.of(2024, 5, 1), "CC123");
        assertEquals(expected, result);
    }
}
