package ru.sapegin.service;

import ru.sapegin.dto.ProductDTO;
import ru.sapegin.model.Product;

public interface ProductServiceI {

    ProductDTO create(ProductDTO productDTO);

    ProductDTO update(Long id, ProductDTO productDTO);

    void delete(Long id);

    Product getProductById(Long id);

    ProductDTO mapToProductDTO(Product product);
}
