package com.minglemate.minglemate.repository;

import com.minglemate.minglemate.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
