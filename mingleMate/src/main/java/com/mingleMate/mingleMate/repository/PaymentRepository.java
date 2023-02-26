package com.mingleMate.mingleMate.repository;

import com.mingleMate.mingleMate.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
