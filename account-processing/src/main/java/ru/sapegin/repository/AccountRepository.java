package ru.sapegin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sapegin.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
}
