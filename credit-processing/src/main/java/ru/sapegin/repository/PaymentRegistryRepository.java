package ru.sapegin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sapegin.model.PaymentRegistry;
import ru.sapegin.model.ProductRegistry;

import java.util.List;

@Repository
public interface PaymentRegistryRepository extends JpaRepository<ProductRegistry, Long> {
    List<PaymentRegistry> findByProductRegistryId(Long productRegistryId);
}
