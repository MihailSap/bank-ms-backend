package ru.sapegin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sapegin.dto.ProductDTO;
import ru.sapegin.model.Product;
import ru.sapegin.repository.ProductRepository;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public ProductDTO create(ProductDTO productDTO) {

        var product = new Product(productDTO.getName(), productDTO.getKey(), LocalDate.now());
        productRepository.save(product);
        log.info("СОЗДАН Product: {}", product);
        return mapToProductDTO(product);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO productDTO){
        var product = getProductById(id);
        product.setName(productDTO.getName());
        product.setKey(productDTO.getKey());
        product.setProductId(String.format("%s%d", productDTO.getKey(), id));
        productRepository.save(product);
        log.info("ОБНОВЛЕН Product: {}", product);
        return mapToProductDTO(product);
    }

    @Transactional
    public void delete(Long id){
        var product = getProductById(id);
        productRepository.delete(product);
        log.info("УДАЛЁН Product: {}", product);
    }

    public Product getProductById(Long id){
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public ProductDTO mapToProductDTO(Product product){
        return new ProductDTO(product.getName(), product.getKey(),
                product.getCreateDate(), product.getProductId());
    }
}