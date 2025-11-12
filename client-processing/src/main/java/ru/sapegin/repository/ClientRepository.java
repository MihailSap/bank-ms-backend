package ru.sapegin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sapegin.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}
