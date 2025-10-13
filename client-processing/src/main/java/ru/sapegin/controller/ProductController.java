package ru.sapegin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.sapegin.dto.ProductDTO;
import ru.sapegin.service.impl.ProductServiceImpl;

@Slf4j
@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ProductController {

    private final ProductServiceImpl productService;

    @PreAuthorize("hasRole('MASTER')")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO create(@RequestBody ProductDTO productDTO){
        return productService.create(productDTO);
    }

    @PreAuthorize("hasAnyRole('MASTER', 'GRAND_EMPLOYEE')")
    @PutMapping("/{id}/update")
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO update(@PathVariable("id") Long id, @RequestBody ProductDTO productDTO){
        return productService.update(id, productDTO);
    }

    @PreAuthorize("hasAnyRole('MASTER', 'GRAND_EMPLOYEE')")
    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") Long id){
        productService.delete(id);
    }
}
