package ru.sapegin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sapegin.model.ClientProduct;

@Repository
public interface ClientProductRepository extends JpaRepository<ClientProduct, Long> {
}
