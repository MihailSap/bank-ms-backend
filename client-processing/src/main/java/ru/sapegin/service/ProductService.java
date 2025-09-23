package ru.sapegin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sapegin.dto.ProductDTO;
import ru.sapegin.model.Product;
import ru.sapegin.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductDTO create(ProductDTO productDTO) {
        var product = new Product(productDTO.getName(), productDTO.getKey(), productDTO.getCreateDate());
        product = productRepository.save(product);
        return mapToProductDTO(product);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO productDTO){
        var product = getProductById(id);
        product.setName(productDTO.getName());
        product.setKey(productDTO.getKey());
        product.setCreateDate(productDTO.getCreateDate());
        product.setProductId(String.format("%s%d", productDTO.getKey(), id));
        productRepository.save(product);
        return mapToProductDTO(product);
    }

    @Transactional
    public void delete(Long id){
        var product = getProductById(id);
        productRepository.delete(product);
    }

    public Product getProductById(Long id){
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public ProductDTO mapToProductDTO(Product product){
        return new ProductDTO(product.getId(), product.getName(), product.getKey(),
                product.getCreateDate(), product.getProductId());
    }
    public Product mapToProduct(ProductDTO productDTO){
        var product = new Product(productDTO.getName(), productDTO.getKey(), productDTO.getCreateDate());
        product.setProductId(String.format("%s%d", productDTO.getKey(), product.getId()));
        return product;
    }
}
