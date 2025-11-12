package ru.sapegin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sapegin.model.ProductRegistry;

@Repository
public interface ProductRegistryRepository extends JpaRepository<ProductRegistry, Long> {
}
