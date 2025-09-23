package ru.sapegin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sapegin.model.BlacklistRegistry;

import java.util.Optional;

@Repository
public interface BlacklistRegistryRepository extends JpaRepository<BlacklistRegistry, Long> {
    Optional<BlacklistRegistry> findByDocumentId(Long documentId);
}
