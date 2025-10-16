package ru.sapegin.service;

import ru.sapegin.dto.ProductDTO;
import ru.sapegin.model.Product;

public interface ProductServiceI {

    Product create(ProductDTO productDTO);

    Product update(Long id, ProductDTO productDTO);

    void delete(Long id);

    Product getProductById(Long id);

    ProductDTO mapToDTO(Product product);
}
