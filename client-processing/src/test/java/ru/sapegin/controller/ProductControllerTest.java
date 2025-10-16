package ru.sapegin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.sapegin.dto.ProductDTO;
import ru.sapegin.enums.KeyEnum;
import ru.sapegin.model.Product;
import ru.sapegin.repository.ProductRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void init() {
        productRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "MASTER")
    void createProductMasterTest() throws Exception {
        ProductDTO request = new ProductDTO();
        request.setName("my_product");
        request.setKey(KeyEnum.DC);

        String response = mockMvc.perform(post("/api/product/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("my_product"))
                .andExpect(jsonPath("$.key").value("DC"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        ProductDTO responseDTO = objectMapper.readValue(response, ProductDTO.class);
        Product saved = productRepository.findAll().stream()
                .filter(p -> p.getProductId().equals(responseDTO.getProductId()))
                .findFirst()
                .orElse(null);

        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo("my_product");
        assertThat(saved.getKey()).isEqualTo(KeyEnum.DC);
    }

    @Test
    @WithMockUser(roles = "USER")
    void createProductUserTest() throws Exception {
        ProductDTO request = new ProductDTO();
        mockMvc.perform(post("/api/product/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MASTER")
    void deleteProductMasterTest() throws Exception {
        Product product = new Product();
        productRepository.save(product);
        mockMvc.perform(delete("/api/product/{id}/delete", product.getId()))
                .andExpect(status().isOk());

        boolean exists = productRepository.existsById(product.getId());
        assertThat(exists).isFalse();
    }

    @Test
    @WithMockUser(roles = "GRAND_EMPLOYEE")
    void deleteProductGrandEmployeeTest() throws Exception {
        Product product = new Product();
        productRepository.save(product);
        mockMvc.perform(delete("/api/product/{id}/delete", product.getId()))
                .andExpect(status().isOk());

        assertThat(productRepository.existsById(product.getId())).isFalse();
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteProductUserTest() throws Exception {
        Product product = new Product();
        productRepository.save(product);
        mockMvc.perform(delete("/api/product/{id}/delete", product.getId()))
                .andExpect(status().isForbidden());

        assertThat(productRepository.existsById(product.getId())).isTrue();
    }

    @Test
    @WithMockUser(roles = "MASTER")
    void updateProductMasterTest() throws Exception {
        var newProduct = new Product();
        newProduct.setName("old_name");
        newProduct.setKey(KeyEnum.DC);
        Product saved = productRepository.save(newProduct);

        ProductDTO updateDto = new ProductDTO();
        updateDto.setName("new_name");
        updateDto.setKey(KeyEnum.AC);
        mockMvc.perform(put("/api/product/{id}/update", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("new_name"))
                .andExpect(jsonPath("$.key").value("AC"));

        Product updated = productRepository.findById(saved.getId()).orElseThrow();
        assertThat(updated.getName()).isEqualTo("new_name");
        assertThat(updated.getKey()).isEqualTo(KeyEnum.AC);
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateProductUserTest() throws Exception {
        var newProduct = new Product();
        newProduct.setName("old_name");
        newProduct.setKey(KeyEnum.DC);
        Product saved = productRepository.save(newProduct);

        ProductDTO updateDto = new ProductDTO();
        updateDto.setName("new_name");
        updateDto.setKey(KeyEnum.AC);
        mockMvc.perform(put("/api/product/{id}/update", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isForbidden());

        Product notUpdated = productRepository.findById(saved.getId()).orElseThrow();
        assertThat(notUpdated.getName()).isEqualTo("old_name");
        assertThat(notUpdated.getKey()).isEqualTo(KeyEnum.DC);
    }
}