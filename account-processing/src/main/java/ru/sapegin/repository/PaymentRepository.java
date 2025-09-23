package ru.sapegin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sapegin.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
