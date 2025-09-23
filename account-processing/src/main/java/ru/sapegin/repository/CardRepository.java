package ru.sapegin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sapegin.model.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {
}
