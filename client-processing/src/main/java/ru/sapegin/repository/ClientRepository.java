package ru.sapegin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.sapegin.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    @Query("SELECT COUNT(c) FROM Client c WHERE c.clientId LIKE :prefix%")
    int countByRegionAndDivision(@Param("prefix") String prefix);
}
