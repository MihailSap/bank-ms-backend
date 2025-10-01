package ru.sapegin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sapegin.model.ErrorLog;

public interface ErrorLogRepository extends JpaRepository<ErrorLog, Long> {
}
