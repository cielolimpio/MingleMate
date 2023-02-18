package com.gongyeon.gongyeon.repository;

import com.gongyeon.gongyeon.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
